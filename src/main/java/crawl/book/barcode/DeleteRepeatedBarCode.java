package crawl.book.barcode;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import crawl.book.barcode.bean.BarCode;
import crawl.util.BarCodeDao;
import util.Constants;
import util.HibernateUtil;

/**
 * 删除重复barCode
 * 
 * @author Administrator
 *
 */
public class DeleteRepeatedBarCode {
	private static String REPEATED_BAR_CODE_FILENAME = "repeatedBarCode.txt";

	/**
	 * 通过mysql查询结果的barCode列表删除
	 */
	public static void deleteBySqlQueryResult() {
		// 读barCode列表
		List<String> repeatedBarCodeList = null;
		try {
			repeatedBarCodeList = FileUtils
					.readLines(new File(Constants.RESOURCES_BASE_PATH, REPEATED_BAR_CODE_FILENAME), Constants.CHARSET);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (CollectionUtils.isEmpty(repeatedBarCodeList)) {
			return;
		}
		// 遍历重复的列表
		// 多线程
		ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_AMOUNT);
		for (String repeatedBarCode : repeatedBarCodeList) {
			// 如果没有条码号，跳过
			if (StringUtils.isEmpty(repeatedBarCode)) {
				continue;
			}
			// 查询每一个重复的barCode
			List<BarCode> findBarCodes = BarCodeDao.findBarCodeByBarCodeString(repeatedBarCode);
			// 如果没有，或者只有一个，则跳过
			if (CollectionUtils.isEmpty(findBarCodes) || findBarCodes.size() == 1) {
				continue;
			}
			// 按id升序排序，为了删除后来的
			Collections.sort(findBarCodes, new Comparator<BarCode>() {
				@Override
				public int compare(BarCode b1, BarCode b2) {
					return b1.getId() - b2.getId();
				}
			});
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// 删除重复的
					for (int i = 1; i < findBarCodes.size(); i++) {
						BarCode barCode = findBarCodes.get(i);
						System.out.println(Thread.currentThread().getName() + " i=" + i + " " + barCode.getId() + " "
								+ barCode.getBarCode());
						HibernateUtil.delete(findBarCodes.get(i));
					}
				}
			});
		}
		executorService.shutdown();
	}

	public static void main(String[] args) {
		deleteBySqlQueryResult();
	}

}
