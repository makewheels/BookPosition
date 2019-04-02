package run;

import java.io.File;
import java.util.List;

import org.dom4j.Element;
import org.junit.Test;

import crawl.classes.secondclass.SecondClassReader;
import crawl.classes.secondclass.bean.SecondClass;
import util.Constants;
import util.XmlParser;

/**
 * 统计书总数
 * 
 * @author Administrator
 *
 */
public class CountBookTypeAmount {

	/**
	 * 根据一级分类统计
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void countByFirstClass() {
		int count = 0;
		File countFile = new File(Constants.RESOURCES_BASE_PATH, "/class/firstClassCount.xml");
		Element rootElement = XmlParser.parseFile(countFile);
		List<Element> elements = rootElement.elements();
		for (int i = 0; i < elements.size(); i++) {
			count += Integer.parseInt(rootElement.element("record" + i).getText());
		}
		System.out.println(count);
	}

	/**
	 * 根据二级分类统计
	 */
	@Test
	public void countBySecondClass() {
		int count = 0;
		List<SecondClass> secondClassList = SecondClassReader.getSecondClassList();
		for (SecondClass secondClass : secondClassList) {
			count += secondClass.getCount();
		}
		System.out.println(count);
	}

}
