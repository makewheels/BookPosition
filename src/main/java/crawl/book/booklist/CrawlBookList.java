package crawl.book.booklist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawl.book.bean.Book;
import crawl.book.booklist.urllist.UrlReader;
import crawl.book.booklist.urllist.bean.BookListUrl;
import crawl.util.BookDao;
import proxy.util.CrawlUtil;
import us.codecraft.xsoup.Xsoup;
import util.Constants;
import util.HibernateUtil;
import util.XmlParser;

/**
 * 爬书列表
 * 
 * @author Administrator
 *
 */
public class CrawlBookList {

	/**
	 * 从html解析出book列表
	 * 
	 * @param html
	 */
	@SuppressWarnings("unchecked")
	private static List<Book> parseHtmlToBookList(String html) {
		Elements trs = Xsoup.select(html, "//*[@id=\"resultTile\"]/div[2]/table/tbody").getElements().get(0).children();
		List<Book> bookList = new ArrayList<>();
		for (Element tr : trs) {
			Element image = tr.child(0).child(0).child(0);
			String coverImageUrl = image.attr("src");
			if (coverImageUrl.contains(";jsessionid=")) {
				coverImageUrl = coverImageUrl.substring(0, coverImageUrl.indexOf(";jsessionid="));
			}
			String isbn = image.attr("isbn");
			Element bookMetaDiv = tr.child(1).child(0);
			String bookrecno = bookMetaDiv.attr("bookrecno");
			Element div0 = bookMetaDiv.child(0);
			String no = div0.ownText();
			if (no.endsWith(" .")) {
				no = no.substring(0, no.length() - 2);
			}
			String name = div0.child(0).child(0).text();
			Element div1 = bookMetaDiv.child(1);
			String author = div1.child(0).text();
			Element div2 = bookMetaDiv.child(2);
			String publisher = div2.child(0).text();
			String publishDate = div2.ownText();
			if (publishDate.contains("出版日期:")) {
				publishDate = publishDate.substring(publishDate.lastIndexOf("出版日期:") + 5, publishDate.length());
				publishDate = publishDate.trim();
			}
			Element div3 = bookMetaDiv.child(3);
			String type = StringUtils.substringBetween(div3.ownText(), "文献类型:", ", 索书号:").trim();
			Book book = new Book(null, no, isbn, coverImageUrl, name, bookrecno, author, publisher, publishDate, type,
					null, null, null, null);
			bookList.add(book);
		}
		// 再发请求，获取索书号
		StringBuilder bookrecnos = new StringBuilder();
		for (Book book : bookList) {
			bookrecnos.append(book.getBookrecno() + ",");
		}
		bookrecnos.deleteCharAt(bookrecnos.length() - 1);
		String xml = null;
		try {
			xml = CrawlUtil.get(Constants.BASE_URL + "/opac/book/callnos?bookrecnos="
					+ URLEncoder.encode(bookrecnos.toString(), Constants.CHARSET));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 解析xml
		List<org.dom4j.Element> records = XmlParser.parseText(xml).elements();
		// 设置到每一个book中
		for (int i = 0; i < bookList.size(); i++) {
			Book book = bookList.get(i);
			String bookrecno = book.getBookrecno();
			for (org.dom4j.Element record : records) {
				if (bookrecno.equals(record.element("bookrecno").getText())) {
					String callno = record.element("callno").getText();
					book.setCallno(callno);
					break;
				}
			}
		}
		return bookList;

	}

	/**
	 * 爬书的列表页
	 */
	public static void main(String[] args) {
		int pageCount = 0;
		int bookCount = 0;
		// 总页数
		long totalPageAmount;
		Session session = HibernateUtil.getSession();
		Query<Long> query = session.createQuery("select count(*) from BookListUrl where isCrawled=false", Long.class);
		totalPageAmount = query.uniqueResult();
		// 拿到未爬的url列表
		List<BookListUrl> bookListUrls = UrlReader.fromDatabase(false);
		// 遍历每一个url
		for (BookListUrl bookListUrl : bookListUrls) {
			String url = bookListUrl.getUrl();
			// 发请求
			String html = CrawlUtil.get(url);
			// 解析html
			List<Book> bookList = parseHtmlToBookList(html);
			// 遍历每一个book
			for (Book book : bookList) {
				// 先根据bookrecno查数据库，看是不是已经有这本书了
				List<Book> findBookList = BookDao.findBookByBookrecno(book.getBookrecno());
				// 如果已经存在这本书，则跳过
				if (CollectionUtils.isNotEmpty(findBookList)) {
					continue;
				}
				// 保存book
				book.setCreateDate(new Date());
				book.setFromUrl(url);
				System.out.println(book.getName());
				HibernateUtil.save(book);
				bookCount++;
			}
			// 更新bookListUrl
			bookListUrl.setIsCrawled(true);
			bookListUrl.setCrawlDate(new Date());
			HibernateUtil.update(bookListUrl);
			// 多线程异步更新每一个book在数据库中的holdingjson
//			ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_AMOUNT);
//			for (Book book : bookList) {
//				executorService.submit(new Runnable() {
//					@Override
//					public void run() {
//						BookHelper.updateHoldingJson(book);
//						HibernateUtil.update(book);
//					}
//				});
//			}
//			executorService.shutdown();
			// 进度信息
			pageCount++;
			int currentPageId = bookListUrl.getId();
			String percent = String.format("%.2f", currentPageId * 1.0 / totalPageAmount * 100) + "%";
			int restPageAmount = (int) (totalPageAmount - currentPageId);
			int remainMinutes = (int) (restPageAmount * 2 * Constants.WAIT_TIME_MILLIS) / 1000 / 60;
			System.err.println(percent + " remain=" + remainMinutes + " page(" + currentPageId + "/" + restPageAmount
					+ ")" + " pageCount=" + pageCount + " bookCount=" + bookCount);
		}
	}

}
