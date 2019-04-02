package crawl.position;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.bean.BarCode;
import crawl.util.BookHelper;
import util.Constants;
import util.HibernateUtil;

/**
 * 从内网爬图书定位
 * 
 * @author Administrator
 *
 */
public class CrawlBookPosition {

	public static void main(String[] args) {
		// 查总数
		Session session = HibernateUtil.getSession();
		Long missionAmount = session.createQuery("select count(*) from BarCode where position=NULL", Long.class)
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
			Query<BarCode> query = session.createQuery("from BarCode where position=NULL", BarCode.class);
			query.setFirstResult(i * maxResult);
			query.setMaxResults(maxResult);
			List<BarCode> barCodeList = query.list();
			// 遍历每一个BarCode
			for (BarCode barCode : barCodeList) {
				String barCodeString = barCode.getBarCode();
				// 如果没有条码号就跳过
				if (StringUtils.isEmpty(barCodeString)) {
					continue;
				}
				// 查图书定位
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						String html = BookHelper.getPositionHtml(barCodeString);
						String position = StringUtils.substringBetween(html, "var strWZxxxxxx = \"", "\";");
						String message = StringUtils.substringBetween(html, "var strMsg = \"", "\";");
						if (BookHelper.isDaqingLibarayBarCode(barCode) == false) {
							System.err.println(Thread.currentThread().getName() + " IS NOT DQLIB BOOK! barCodeId="
									+ barCodeString);
							return;
						}
						barCode.setPosition(position);
						barCode.setMessage(message);
						barCode.setGetPositionDate(new Date());
						HibernateUtil.update(barCode);
						System.out.println(Thread.currentThread().getName() + " barCodeId=" + barCode.getId()
								+ " barCodeString=" + barCodeString + " position=" + barCode.getPosition() + " message="
								+ barCode.getMessage() + ".END");
					}
				});
			}
		}
		executorService.shutdown();
	}

}
