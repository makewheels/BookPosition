package prepare.firstclass;

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

import util.Constants;
import util.HttpUtil;

/**
 * 爬分类
 * 
 * @author Administrator
 *
 */
public class CrawlFirstClass {
	// 一级分类xml文件相对路径
	private String fistClassFilePath = "/class/firstClass.xml";

	/**
	 * 爬一级分类
	 * 
	 * @throws IOException
	 */
	@Test
	public void crawlFirstClass() throws IOException {
		String firstClassXml = HttpUtil.get(Constants.BASE_URL + "/opac/browse/query?category=cls&id=0");
		FileUtils.writeStringToFile(new File(Constants.RESOURCES_BASE_PATH, fistClassFilePath), firstClassXml,
				Constants.CHARSET);
	}

	/**
	 * 爬二级分类列表
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * @throws InterruptedException
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void crawlSecondClass() throws IOException, DocumentException, InterruptedException {
		Document firstClassDocument = new SAXReader().read(new File(Constants.RESOURCES_BASE_PATH, fistClassFilePath));
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
	public void crawlSecondClassCount() throws DocumentException, IOException, InterruptedException {
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
