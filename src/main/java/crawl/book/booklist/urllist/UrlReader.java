package crawl.book.booklist.urllist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.book.booklist.urllist.bean.BookListUrl;
import util.Constants;
import util.HibernateUtil;

/**
 * 读要爬的url列表
 * 
 * @author Administrator
 *
 */
public class UrlReader {
	private static List<String> urlList;

	/**
	 * 从本地文件读url列表
	 * 
	 * @return
	 */
	public static List<String> fromLocalFile() {
		if (urlList != null) {
			return urlList;
		}
		urlList = new ArrayList<>();
		try {
			urlList = FileUtils.readLines(new File(Constants.RESOURCES_BASE_PATH, "/mission/urlList"),
					Constants.CHARSET);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlList;
	}

	/**
	 * 从数据库读url列表
	 * 
	 * @param isCrawled 是否已经爬过
	 * @return
	 */
	public static List<BookListUrl> fromDatabase(boolean isCrawled) {
		Session session = HibernateUtil.getSession();
		Query<BookListUrl> query = session.createQuery("from BookListUrl where isCrawled=?1", BookListUrl.class);
		query.setParameter(1, false);
		return query.list();
	}

}
