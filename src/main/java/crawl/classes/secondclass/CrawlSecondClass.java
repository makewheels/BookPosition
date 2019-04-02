package crawl.classes.secondclass;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import crawl.classes.firstclass.CrawlFirstClass;
import util.Constants;
import util.HttpUtil;

public class CrawlSecondClass {

	/**
	 * 爬二级分类列表
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * @throws InterruptedException
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void crawlList() throws IOException, DocumentException, InterruptedException {
		Document firstClassDocument = new SAXReader()
				.read(new File(Constants.RESOURCES_BASE_PATH, CrawlFirstClass.FIRST_CLASS_FILE_PATH));
		List<Element> elements = firstClassDocument.getRootElement().elements();
		for (Element record : elements) {
			String secondClassId = record.element("id").getText();
			String secondClassXml = HttpUtil
					.get(Constants.BASE_URL + "/opac/browse/query?category=cls&id=" + secondClassId);
			FileUtils.writeStringToFile(
					new File(Constants.RESOURCES_BASE_PATH + "/class/second/list/" + secondClassId + ".xml"),
					secondClassXml, Constants.CHARSET);
			System.out.println(secondClassId + " " + secondClassXml);
			Thread.sleep(Constants.WAIT_TIME_MILLIS);
		}
	}

	/**
	 * 爬二级分类总数
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void crawlAmount() throws DocumentException, IOException, InterruptedException {
		// 所有二级分类xml文件
		File[] listFiles = new File(Constants.RESOURCES_BASE_PATH, "/class/second/list").listFiles();
		// 遍历每一个文件
		for (File file : listFiles) {
			Element rootElement = new SAXReader().read(file).getRootElement();
			// 拼装请求参数
			StringBuilder classnos = new StringBuilder();
			// 遍历每一个record
			List<Element> elements = rootElement.elements();
			for (Element record : elements) {
				String name = record.element("name").getText();
				classnos.append(name + "]]");
			}
			classnos.delete(classnos.length() - 2, classnos.length());
			Map<String, String> param = new HashMap<>();
			param.put("category", "cls");
			param.put("classnos", classnos.toString());
			param.put("libcode", "");
			param.put("booktype", "");
			param.put("curlocal", "");
			String xml = HttpUtil.post(Constants.BASE_URL + "/opac/browse/count", param);
			System.out.println(file.getName() + " " + xml);
			FileUtils.writeStringToFile(
					new File(Constants.RESOURCES_BASE_PATH,
							"/class/second/count/" + FilenameUtils.getBaseName(file.getName()) + ".xml"),
					xml, Constants.CHARSET);
			Thread.sleep(Constants.WAIT_TIME_MILLIS);
		}
	}

}
