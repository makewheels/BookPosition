package run;

import java.io.File;
import java.util.List;

import org.dom4j.Element;
import org.junit.Test;

import util.Constants;
import util.XmlParser;

/**
 * 统计书总数
 * 
 * @author Administrator
 *
 */
public class CountBookAmount {

	@Test
	@SuppressWarnings("unchecked")
	public void count() {
		int count = 0;
		File[] listFiles = new File(Constants.RESOURCES_BASE_PATH, "/class/second/count").listFiles();
		for (File file : listFiles) {
			Element rootElement = XmlParser.getRootElement(file);
			List<Element> elements = rootElement.elements();
			for (Element element : elements) {
				count += Integer.parseInt(element.getText());
			}
		}
		System.out.println(count);
	}

}
