package crawl.util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.book.barcode.bean.BarCode;
import util.HibernateUtil;

public class BarCodeDao {

	/**
	 * 通过条码号查找BarCode
	 * 
	 * @param barCodeString
	 * @return
	 */
	public static List<BarCode> findBarCodeByBarCodeString(String barCodeString) {
		Session session = HibernateUtil.getSession();
		Query<BarCode> query = session.createQuery("from BarCode where barCode=?1", BarCode.class);
		query.setParameter(1, barCodeString);
		return query.list();
	}

}
