package proxy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import proxy.bean.ProxyIp;
import us.codecraft.xsoup.Xsoup;
import util.HttpUtil;

/**
 * 爬取代理ip https://www.xicidaili.com
 * 
 * @author Administrator
 *
 */
public class CrawlProxyIp {
	private static String BASE_URL = "https://www.xicidaili.com/";
	public static String TYPE_HTTP = "wt/";
	public static String TYPE_HTTPS = "wn/";

	/**
	 * 根据http还是http类型
	 * 
	 * @param type
	 * @param page
	 * @return 要爬的url
	 */
	private static String getUrl(String type, int page) {
		if (type.equals(TYPE_HTTP)) {
			return BASE_URL + TYPE_HTTP + page;
		} else {
			return BASE_URL + TYPE_HTTPS + page;
		}
	}

	/**
	 * 根据多个tr节点解析出ProxyIp对象集合
	 * 
	 * @param trs
	 * @return
	 */
	private static List<ProxyIp> parseTrsToBeans(Elements trs) {
		List<ProxyIp> proxyIps = new ArrayList<>();
		for (Element tr : trs) {
			Elements tds = tr.children();
			String ip = tds.get(1).text();
			int port = Integer.parseInt(tds.get(2).text());
			String address = tds.get(3).text();
			boolean isAnonymous;
			String isAnonymouString = tds.get(4).text();
			if (isAnonymouString.equals("高匿")) {
				isAnonymous = true;
			} else {
				isAnonymous = false;
				// 只要高匿
				// continue;
			}
			String type = tds.get(5).text();
			String speedTimeString = tds.get(6).child(0).attr("title");
			long speed = 0;
			if (speedTimeString.endsWith("秒")) {
				speedTimeString = speedTimeString.substring(0, speedTimeString.length() - 1);
				speed = (long) (Double.parseDouble(speedTimeString) * 1000);
			}
			String connectTimeString = tds.get(7).child(0).attr("title");
			long connectTime = 0;
			if (connectTimeString.endsWith("秒")) {
				connectTimeString = connectTimeString.substring(0, connectTimeString.length() - 1);
				connectTime = (long) (Double.parseDouble(connectTimeString) * 1000);
			}
			String aliveTimeString = tds.get(8).text();
			long aliveTime = 0;
			if (aliveTimeString.endsWith("分钟")) {
				aliveTimeString = aliveTimeString.substring(0, aliveTimeString.length() - 2);
				aliveTime = Long.parseLong(aliveTimeString) * 60 * 1000;
			} else if (aliveTimeString.endsWith("小时")) {
				aliveTimeString = aliveTimeString.substring(0, aliveTimeString.length() - 2);
				aliveTime = Long.parseLong(aliveTimeString) * 60 * 60 * 1000;
			} else if (aliveTimeString.endsWith("天")) {
				aliveTimeString = aliveTimeString.substring(0, aliveTimeString.length() - 1);
				aliveTime = Long.parseLong(aliveTimeString) * 24 * 60 * 60 * 1000;
			}
			String validateTimesString = tds.get(9).text();
			long validateTime = 0;
			try {
				validateTime = new SimpleDateFormat("yy-MM-dd HH:mm").parse(validateTimesString).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ProxyIp proxyIp = new ProxyIp(ip, port, address, isAnonymous, type, speed, connectTime, aliveTime,
					validateTime);
			proxyIps.add(proxyIp);
		}
		return proxyIps;
	}

	/**
	 * 类型：是http还是https
	 * 
	 * @param type 用本类常量
	 * @param page 页码
	 * @return
	 */
	public static List<ProxyIp> getProxyIpList(String type, int page) {
		String url = getUrl(type, page);
		Elements trs = Xsoup.select(HttpUtil.get(url), "//*[@id=\"ip_list\"]/tbody").getElements().get(0).children();
		trs.remove(0);
		return parseTrsToBeans(trs);
	}

	@Test
	public void testCrwalProxyIps() {
		List<ProxyIp> proxyIpList = getProxyIpList(TYPE_HTTP, 1);
		for (ProxyIp proxyIp : proxyIpList) {
			System.out.println(proxyIp);
		}
	}

}
