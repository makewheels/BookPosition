package prepare;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import prepare.secondclass.SecondClassReader;
import prepare.secondclass.bean.SecondClass;
import util.Constants;

/**
 * 生成要爬取书详情页的url列表
 * 
 * @author Administrator
 *
 */
public class GenerateBookInfoUrls {
	private static int pageSize = 10;

	public static void generate() {
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
							+ "&searchType=standard&isFacet=false&view=simple&searchWay=class&rows=10&sortWay=score&sortOrder=desc&searchWay0=marc&logical0=AND&page="
							+ i;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				urlList.add(url);
			}
		}
		try {
			FileUtils.writeLines(new File(Constants.RESOURCES_BASE_PATH, "/mission/urlList"), urlList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		generate();
	}
}
