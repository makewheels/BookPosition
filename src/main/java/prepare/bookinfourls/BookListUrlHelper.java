package prepare.bookinfourls;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import crawl.bean.BookListUrl;
import prepare.secondclass.SecondClassReader;
import prepare.secondclass.bean.SecondClass;
import util.Constants;
import util.HibernateUtil;

/**
 * 详情页的url列表
 * 
 * @author Administrator
 *
 */
public class BookListUrlHelper {
	private static int pageSize = 200;

	/**
	 * 返回url列表
	 * 
	 * @return
	 */
	public static List<String> getUrlList() {
		List<SecondClass> secondClassList = SecondClassReader.getSecondClassList();
		List<String> urlList = new ArrayList<>();
		for (SecondClass secondClass : secondClassList) {
			String name = secondClass.getName();
			Integer count = secondClass.getCount();
			int totalPage = (int) Math.ceil(count * 1.0 / pageSize);
			for (int i = 1; i <= totalPage; i++) {
				String url = null;
				try {
					url = Constants.BASE_URL_EXTERNAL + "/opac/search?q=" + URLEncoder.encode(name, Constants.CHARSET)
							+ "&searchType=standard&isFacet=false&view=simple&searchWay=class&rows=" + pageSize
							+ "&sortWay=score&sortOrder=desc&searchWay0=marc&logical0=AND&page=" + i;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				urlList.add(url);
			}
		}
		return urlList;
	}

	@Test
	public static void saveToFile() throws IOException {
		List<String> urlList = getUrlList();
		FileUtils.writeLines(new File(Constants.RESOURCES_BASE_PATH, "/mission/urlList"), urlList);
		System.out.println("Done");
	}

	@Test
	public static void saveToDatabase() {
		List<String> urlList = getUrlList();
		for (String url : urlList) {
			BookListUrl bookInfoUrl = new BookListUrl();
			bookInfoUrl.setUrl(url);
			bookInfoUrl.setCreateDate(new Date());
			bookInfoUrl.setIsCrawled(false);
			HibernateUtil.save(bookInfoUrl);
		}
	}

	public static void main(String[] args) {
		saveToDatabase();
	}
}
