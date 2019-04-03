package crawl.position;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.book.barcode.bean.BarCode;
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
		Long missionAmount = session.createQuery("select count(*) from BarCode where position IS NULL", Long.class)
				.getSingleResult();
		// 分页查询
		// 每页多少个
		int maxResult = 1000;
		// 总页数
		int totalPage = (int) (missionAmount / maxResult);
		System.out.println("totalPage:" + totalPage);
		// 多线程
		ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_AMOUNT);
		for (int i = 0; i < totalPage + 1; i++) {
			// 逐页查询
			Query<BarCode> query = session.createQuery("from BarCode where position IS NULL", BarCode.class);
			query.setFirstResult(i * maxResult);
			query.setMaxResults(maxResult);
			List<BarCode> barCodeList = query.list();
			// 遍历每一个BarCode，查图书定位
			for (BarCode barCode : barCodeList) {
				String barCodeString = barCode.getBarCode().trim();
				// 如果没有条码号就跳过
				if (StringUtils.isEmpty(barCodeString)) {
					continue;
				}
				// 如果已经有位置信息了，跳过
				if (barCode.getPosition() != null || barCode.getMessage() != null) {
					continue;
				}
				// 提交任务
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						// 如果是大庆市图书馆的书
						if (BookHelper.isDaqingLibarayBarCode(barCode)) {
							// 发请求
							String html = BookHelper.getPositionHtml(barCodeString);
							// 解析
							String position = StringUtils.substringBetween(html, "var strWZxxxxxx = \"", "\";");
							String message = StringUtils.substringBetween(html, "var strMsg = \"", "\";");
							// 如果没拿到，则跳过
							if (position == null || message == null) {
								System.err.println("解析position或message错误！");
								return;
							}
							// 更新数据库
							barCode.setPosition(position);
							barCode.setMessage(message);
							barCode.setGetPositionDate(new Date());
//							HibernateUtil.update(barCode);
							System.err.println(Thread.currentThread().getName() + " barCodeId=" + barCode.getId()
									+ " barCode=" + barCodeString + " position=" + barCode.getPosition() + " message="
									+ barCode.getMessage() + ".END");
						}
						// 线程池：开始
					}
				});
				// 线程池：结束
			}
		}
		executorService.shutdown();
	}

}
