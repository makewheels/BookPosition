package crawl.classes.firstclass;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import util.Constants;
import util.HttpUtil;

/**
 * 爬分类
 * 
 * @author Administrator
 *
 */
public class CrawlFirstClass {
	public static String FIRST_CLASS_FILE_PATH = "/class/firstClassList.xml";

	/**
	 * 爬一级分类列表
	 * 
	 * @throws IOException
	 */
	@Test
	public void crawlFirstClass() throws IOException {
		String firstClassXml = HttpUtil.get(Constants.BASE_URL + "/opac/browse/query?category=cls&id=0");
		FileUtils.writeStringToFile(new File(Constants.RESOURCES_BASE_PATH, FIRST_CLASS_FILE_PATH), firstClassXml,
				Constants.CHARSET);
	}

	/**
	 * 爬一级分类总数
	 * 
	 * @throws IOException
	 */
	@Test
	public void crawlFirstClassAmount() throws IOException {
		Map<String, String> param = new HashMap<>();
		param.put("category", "cls");
		param.put("classnos", "A]]B]]C]]D]]E]]F]]G]]H]]I]]J]]K]]N]]O]]P]]Q]]R]]S]]T]]U]]V]]X]]Z");
		param.put("libcode", "");
		param.put("booktype", "");
		param.put("curlocal", "");
		String firstClassXml = HttpUtil.post(Constants.BASE_URL + "/opac/browse/count", param);
		FileUtils.writeStringToFile(new File(Constants.RESOURCES_BASE_PATH, "/class/firstClassCount.xml"),
				firstClassXml, Constants.CHARSET);
	}
}
