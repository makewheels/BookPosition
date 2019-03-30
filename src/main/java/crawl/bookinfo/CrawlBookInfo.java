package crawl.bookinfo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Selectable;
import util.Constants;
import util.HttpUtil;

/**
 * 爬书的信息
 * 
 * @author Administrator
 *
 */
public class CrawlBookInfo implements PageProcessor {
	private Site site = Site.me().setRetryTimes(3);

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		Selectable tbody = page.getHtml().xpath("//*[@id=\"resultTile\"]/div[2]/table/tbody");
		List<Selectable> trs = tbody.$("tbody>tr").nodes();
		for (Selectable tr : trs) {
			List<Selectable> tds = tr.$("tr>td").nodes();
			Selectable td1 = tds.get(1);
			String bookrecno = td1.$("td>div", "bookrecno").get();
			// 拿到bookrecno之后，请求api拿条码号
			String jsonString = HttpUtil.get(Constants.BASE_URL_EXTERNAL + "/opac/api/holding/" + bookrecno);
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(jsonString).getAsJsonObject();
			JsonObject info = jsonObject.get("holdingList").getAsJsonArray().get(0).getAsJsonObject();
			String barcode = info.get("barcode").getAsString();
			System.out.println(barcode);
			try {
				FileUtils.write(new File(Constants.RESOURCES_BASE_PATH, "/barcode"), barcode, Constants.CHARSET, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Spider spider = Spider.create(new CrawlBookInfo());
		HttpClientDownloader downloader = new HttpClientDownloader();
		downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("101.101.101.101", 8888)));
		spider.setDownloader(downloader);
		spider.thread(5);
		spider.setScheduler(
				new FileCacheQueueScheduler(new File(Constants.RESOURCES_BASE_PATH, "/mission/urlList").getPath()));
		spider.run();
	}

}
