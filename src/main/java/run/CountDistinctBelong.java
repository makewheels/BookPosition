package run;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import crawl.bean.BarCode;
import crawl.bean.Position;
import crawl.util.BookHelper;
import util.HibernateUtil;

public class CountDistinctBelong {

	public static void main(String[] args) {
		// 查总数
		Session session = HibernateUtil.getSession();
		Long missionAmount = session.createQuery("select count(*) from BarCode where position IS NOT NULL", Long.class)
				.getSingleResult();
		// 分页查询
		// 每页多少个
		int maxResult = 1000;
		// 总页数
		int totalPage = (int) (missionAmount / maxResult);
		Set<String> belongSet = new HashSet<>();
		for (int i = 0; i < totalPage + 1; i++) {
			// 逐页查询
			Query<BarCode> query = session.createQuery("from BarCode where position IS NOT NULL", BarCode.class);
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
				Position position = BookHelper.parsePositionFromString(barCode.getPosition());
				if (position == null) {
					continue;
				}
				belongSet.add(position.getBelong());
			}
		}
		System.out.println(belongSet);
	}

}
