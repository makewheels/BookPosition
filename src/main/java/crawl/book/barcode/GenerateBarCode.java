package crawl.book.barcode;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.alibaba.fastjson.JSON;

import crawl.book.barcode.bean.BarCode;
import crawl.book.barcode.bean.BarCodeDetail;
import crawl.book.bean.Book;
import crawl.util.BookHelper;
import util.HibernateUtil;

/**
 * 生成数据库中的条码号
 * 
 * @author Administrator
 *
 */
public class GenerateBarCode {

	/**
	 * 根据数据库中book已保存的holdingJson，解析出条码号，保存到barcode表中
	 */
	public static void generateByDatabase() {
		// 查总数
		Session session = HibernateUtil.getSession();
		Long totalAmount = session.createQuery("select count(*) from Book", Long.class).getSingleResult();
		// 分页查询book
		// 每页多少个
		int maxResult = 1000;
		// 总页数
		int totalPage = (int) (totalAmount / maxResult);
		for (int i = 0; i < totalPage + 1; i++) {
			// 逐页查询
			Query<Book> query = session.createQuery("from Book", Book.class);
			query.setFirstResult(i * maxResult);
			query.setMaxResults(maxResult);
			List<Book> bookList = query.list();
			// 遍历每一个book
			for (Book book : bookList) {
				double percent = book.getId() * 1.0 / totalAmount * 100;
				System.err.println(String.format("%.2f", percent) + "% " + book.getId() + "/" + totalAmount);
				String holdingJson = book.getHoldingJson();
				if (holdingJson == null || holdingJson.equals("")) {
					continue;
				}
				// 通过book中的holdingJson解析出barCode的json列表
				List<String> barCodeJsonList = BookHelper.getBarCodeList(book);
				// 如果没有holdingList，则跳过
				if (barCodeJsonList == null || barCodeJsonList.isEmpty()) {
					continue;
				}
				// 遍历json列表
				for (String barCodeJson : barCodeJsonList) {
					// 解析出每一个条码号
					BarCodeDetail barCodeDetail = JSON.parseObject(barCodeJson, BarCodeDetail.class);
					String barcode = barCodeDetail.getBarcode();
					// 保存barCode
					BarCode barCode = new BarCode(null, book.getBookrecno(), barcode, null, null, new Date(), null,
							barCodeJson);
					HibernateUtil.save(barCode);
				}
			}
		}
	}

	public static void main(String[] args) {
		generateByDatabase();
	}

}
