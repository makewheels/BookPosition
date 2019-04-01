package crawl.bookinfo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.bean.Book;
import crawl.util.BookHelper;
import util.Constants;
import util.HibernateUtil;

/**
 * 更新书的holdingJson
 * 
 * @author Administrator
 *
 */
public class CrawlBookHoldingJson {

	public static void main(String[] args) {
		// 查总数
		Session session = HibernateUtil.getSession();
		Long missionAmount = session.createQuery("select count(*) from Book where holdingJson=NULL", Long.class)
				.getSingleResult();
		// 分页查询
		// 每页多少个
		int maxResult = 1000;
		// 总页数
		int totalPage = (int) (missionAmount / maxResult);
		// 多线程
		ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_AMOUNT);
		for (int i = 0; i < totalPage + 1; i++) {
			// 逐页查询
			Query<Book> query = session.createQuery("from Book where holdingJson=NULL", Book.class);
			query.setFirstResult(i * maxResult);
			query.setMaxResults(maxResult);
			List<Book> bookList = query.list();
			// 遍历每一个book
			for (Book book : bookList) {
				if (StringUtils.isNotEmpty(book.getHoldingJson())) {
					continue;
				}
				// 更新holdingJson
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						BookHelper.updateHoldingJson(book);
						HibernateUtil.update(book);
						System.err.println(Thread.currentThread().getName() + " " + book.getId());
					}
				});
			}
		}
		executorService.shutdown();
	}

}
