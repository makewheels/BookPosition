package util;

public class CrawlUtil {
	private static long lastRequestTime = -1;

	/**
	 * 有频率限制的get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		if (lastRequestTime != -1) {
			long currentTimeMillis = System.currentTimeMillis();
			long diff = currentTimeMillis - lastRequestTime;
			if (diff <= Constants.WAIT_TIME_MILLIS) {
				try {
					System.out.println("wait for " + diff + "ms");
					Thread.sleep(diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lastRequestTime = System.currentTimeMillis();
		}
		return HttpUtil.get(url);
	}

}
