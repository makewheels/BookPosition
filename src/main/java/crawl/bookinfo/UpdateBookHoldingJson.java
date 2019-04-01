package crawl.bookinfo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.bean.Book;
import crawl.util.BookHelper;
import util.HibernateUtil;

/**
 * 更新书的holdingJson
 * 
 * @author Administrator
 *
 */
public class UpdateBookHoldingJson {

	/**
	 * 在数据库中更新单本书的holdingJson
	 * 
	 * @param book
	 */
	public static void updateSingleBook(Book book) {
		BookHelper.updateHoldingJson(book);
		HibernateUtil.update(book);
	}

	public static void main(String[] args) {
		// 查总数
		Session session = HibernateUtil.getSession();
		Long totalAmount = session.createQuery("select count(*) from Book where holdingJson=NULL", Long.class)
				.getSingleResult();
		// 分页查询book
		// 每页多少个
		int maxResult = 1000;
		// 总页数
		int totalPage = (int) (totalAmount / maxResult);
		for (int i = 0; i < totalPage + 1; i++) {
			// 逐页查询
			Query<Book> query = session.createQuery("from Book where holdingJson=NULL", Book.class);
			query.setFirstResult(i * maxResult);
			query.setMaxResults(maxResult);
			List<Book> bookList = query.list();
			// 遍历每一个book
			for (Book book : bookList) {
				// 更新holdingJson
				updateSingleBook(book);
				double percent = book.getId() * 1.0 / totalAmount * 100;
				System.err.println(String.format("%.2f", percent) + "% " + book.getId() + "/" + totalAmount);
			}
		}
	}

}
