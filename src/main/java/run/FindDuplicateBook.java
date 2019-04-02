package run;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.book.bean.Book;
import util.HibernateUtil;

/**
 * 查数据库中bookrecno重复的书
 * 
 * @author Administrator
 *
 */
public class FindDuplicateBook {

	public static void main(String[] args) {
		// 查总数
		Session session = HibernateUtil.getSession();
		Long missionAmount = session.createQuery("select count(*) from Book", Long.class).getSingleResult();
		// 分页查询
		// 每页多少个
		int maxResult = 100;
		// 总页数
		int totalPage = (int) (missionAmount / maxResult);
		Set<String> set = new HashSet<>();
		for (int i = 0; i < totalPage + 1; i++) {
			// 逐页查询
			Query<Book> query = session.createQuery("from Book", Book.class);
			query.setFirstResult(i * maxResult);
			query.setMaxResults(maxResult);
			List<Book> bookList = query.list();
			// 遍历
			for (Book book : bookList) {
				String bookrecno = book.getBookrecno();
				if (set.contains(bookrecno)) {
					System.out.println(book.getId());
				}
				set.add(bookrecno);
			}
		}
		System.out.println("set size=" + set.size());
	}

}
