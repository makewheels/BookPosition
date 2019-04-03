package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate工具类
 * 
 * @author Administrator
 *
 */
public class HibernateUtil {
	private static SessionFactory sessionFactory;

	static {
		init();
	}

	/**
	 * 初始化
	 */
	public static void init() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
	}

	public static Session getSession() {
		if (sessionFactory == null) {
			init();
		}
		return sessionFactory.openSession();
	}

	/**
	 * 根据主键查对象
	 * 
	 * @param clazz
	 * @param primaryKey
	 * @return
	 */
	public static <T> T findObjectById(Class<T> clazz, Object primaryKey) {
		Session session = sessionFactory.openSession();
		T findObject = session.find(clazz, primaryKey);
		session.close();
		return findObject;
	}

	/**
	 * 保存对象
	 * 
	 * @param object
	 */
	public static void save(Object object) {
		Session session = sessionFactory.openSession();
		session.save(object);
		session.close();
	}

	/**
	 * 更新对象
	 * 
	 * @param object
	 */
	public static void update(Object object) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(object);
		transaction.commit();
		session.close();
	}

	/**
	 * 删除对象
	 * 
	 * @param object
	 */
	public static void delete(Object object) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.delete(object);
		transaction.commit();
		session.close();
	}

}
