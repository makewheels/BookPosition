package crawl.util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.book.bean.Book;
import util.HibernateUtil;

public class BookDao {

	/**
	 * 根据bookrecno查找book
	 * 
	 * @param bookrecno
	 * @return
	 */
	public static List<Book> findBookByBookrecno(String bookrecno) {
		Session session = HibernateUtil.getSession();
		Query<Book> query = session.createQuery("from Book where bookrecno=?1", Book.class);
		query.setParameter(1, bookrecno);
		return query.list();
	}

}
