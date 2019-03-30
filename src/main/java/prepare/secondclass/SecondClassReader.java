package prepare.secondclass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import util.Constants;
import util.XmlParser;

/**
 * 读二级分类
 * 
 * @author Administrator
 *
 */
public class SecondClassReader {

	@SuppressWarnings("unchecked")
	public static List<SecondClass> getSecondClassLIst() {
		List<SecondClass> secondClasseList = new ArrayList<>();
		File[] listFiles = new File(Constants.RESOURCES_BASE_PATH, "/class/second/list").listFiles();
		for (File listFile : listFiles) {
			Element listRootElement = XmlParser.getRootElement(listFile);
			List<Element> elements = listRootElement.elements();
			File countFile = new File(Constants.RESOURCES_BASE_PATH, "/class/second/count/" + listFile.getName());
			Element countRootElement = XmlParser.getRootElement(countFile);
			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				String id = element.element("id").getText();
				String parentId = element.element("parentId").getText();
				String name = element.element("name").getText();
				String categoryDesc = element.element("categoryDesc").getText();
				int count = Integer.parseInt(countRootElement.element("record" + i).getText());
				SecondClass secondClass = new SecondClass(id, parentId, name, categoryDesc, count);
				secondClasseList.add(secondClass);
			}
		}
		return secondClasseList;
	}

	public static void main(String[] args) {
		List<SecondClass> secondClassLIst = getSecondClassLIst();
		System.out.println(secondClassLIst.size());
		for (SecondClass secondClass : secondClassLIst) {
			System.out.println(secondClass);
		}
	}
}