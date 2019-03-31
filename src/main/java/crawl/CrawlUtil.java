package crawl;

import java.io.IOException;

import org.apache.http.HttpHost;

import proxy.bean.RequestFrequencyException;
import util.HttpUtil;

public class CrawlUtil {

	/**
	 * 有频率限制的get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String get(String url) {
//		return HttpUtil.get(url);
		try {
			return HttpUtil.getProxy(url, new HttpHost("183.148.155.154", 9999));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestFrequencyException e) {
			e.printStackTrace();
		}
		return null;
	}

}
