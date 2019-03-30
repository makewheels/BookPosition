package crawl.bookinfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import util.Constants;

public class UrlReader {
	private static List<String> urlList;

	/**
	 * 返回所有url
	 * 
	 * @return
	 */
	public static List<String> getUrlList() {
		if (urlList != null) {
			return urlList;
		}
		urlList = new ArrayList<>();
		try {
			urlList = FileUtils.readLines(new File(Constants.RESOURCES_BASE_PATH, "/mission/urlList"),
					Constants.CHARSET);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlList;
	}

}
