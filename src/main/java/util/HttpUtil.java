package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import proxy.bean.RequestFrequencyException;

/**
 * Http工具类
 * 
 * @author Administrator
 *
 */
public class HttpUtil {
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";
	private static String contentType = "application/x-www-form-urlencoded";

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 */
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static HttpGet httpGet = new HttpGet();
	static {
		httpGet.addHeader("User-Agent", userAgent);
		httpGet.setHeader("Content-type", contentType);
		httpGet.setHeader("Connection", "keep-alive");
	}

	public static String get(String url) {
		System.out.println("HttpClient GET: " + url);
		httpGet.setURI(URI.create(url));
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		try {
			return EntityUtils.toString(entity, Constants.CHARSET);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @return
	 */
	public static String post(String url, Map<String, String> param) {
		System.out.println("HttpClient POST: " + url);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		// 装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (param != null) {
			for (Entry<String, String> entry : param.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		// 设置参数到请求对象中
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpPost.setHeader("User-Agent", userAgent);
		httpPost.setHeader("Content-type", contentType);
		String body = null;
		try {
			CloseableHttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				body = EntityUtils.toString(entity, Constants.CHARSET);
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 使用代理ip的get请求 <br>
	 * 如果超过频率限制，抛异常<br>
	 * 如果超时，抛异常
	 * 
	 * @param url
	 * @param proxyHttpHost
	 * @return
	 * @throws IOException
	 * @throws RequestFrequencyException
	 */
	public static String getProxy(String url, HttpHost proxyHttpHost) throws IOException, RequestFrequencyException {
		System.out.println("HttpClient GET PROXY: " + url);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("User-Agent", userAgent);
		httpGet.setHeader("Content-type", contentType);
		if (proxyHttpHost != null) {
			RequestConfig config = RequestConfig.custom().setProxy(proxyHttpHost)
					.setConnectTimeout(Constants.TIME_OUT_MILLIS).build();
			httpGet.setConfig(config);
		}
		CloseableHttpResponse response = client.execute(httpGet);
		String responseString = EntityUtils.toString(response.getEntity(), Constants.CHARSET);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 403) {
			RequestFrequencyException exception = new RequestFrequencyException();
			exception.initCause(new Throwable(responseString));
			throw exception;
		} else {
			return responseString;
		}
	}

}
