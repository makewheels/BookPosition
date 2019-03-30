package crawl.bookinfo;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
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
		}
	}

	public static void main(String[] args) {
		Spider spider = Spider.create(new CrawlBookInfo());
		spider.thread(5);
		spider.setScheduler(new FileCacheQueueScheduler(""));
		spider.addUrl(
				"http://60.218.184.234:8091/opac/search?q=I0&searchType=standard&isFacet=false&view=simple&searchWay=class&rows=10&sortWay=score&sortOrder=desc&searchWay0=marc&logical0=AND&page=1");
		spider.run();
	}

	public void crawlBookRecNo() {
		// http://60.218.184.234:8091/opac/search?isFacet=false&view=simple&searchWay=class&q=I0&classname=I0%E6%96%87%E5%AD%A6%E7%90%86%E8%AE%BA&libcode=&booktype=&curlocal=
	}

}
