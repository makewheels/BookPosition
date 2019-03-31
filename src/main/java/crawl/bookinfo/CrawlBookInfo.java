package crawl.bookinfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import crawl.BookHelper;
import crawl.bean.Book;
import crawl.bean.BookListUrl;
import us.codecraft.xsoup.Xsoup;
import util.Constants;
import util.CrawlUtil;
import util.HibernateUtil;
import util.XmlParser;

/**
 * 爬书详情
 * 
 * @author Administrator
 *
 */
public class CrawlBookInfo {

	/**
	 * 从html解析出book列表
	 * 
	 * @param html
	 */
	@SuppressWarnings("unchecked")
	private List<Book> parseHtmlToBookList(String html) {
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
			xml = CrawlUtil.get(Constants.BASE_URL_EXTERNAL + "/opac/book/callnos?bookrecnos="
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
		// 根据recno查条码号
//		for (Book book : bookList) {
//			book.setBarCode(BookHelper.getBarCodeByRecno(book.getBookrecno()));
//		}
		return bookList;

	}

	/**
	 * 爬书的列表页
	 */
	@Test
	public void crawl() {
		int pageCount = 0;
		int bookCount = 0;
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
				// 保存book
				book.setCreateDate(new Date());
				book.setFromUrl(url);
				HibernateUtil.save(book);
				bookCount++;
				// 更新bookListUrl
				bookListUrl.setIsCrawled(true);
				bookListUrl.setCrawlDate(new Date());
				HibernateUtil.update(bookListUrl);
			}
			pageCount++;
			System.err.println("pageCount = " + pageCount + " bookCount = " + bookCount);
		}
	}

}
