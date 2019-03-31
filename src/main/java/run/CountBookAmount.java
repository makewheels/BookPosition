package run;

import java.util.List;

import org.junit.Test;

import prepare.secondclass.SecondClassReader;
import prepare.secondclass.bean.SecondClass;

/**
 * 统计书总数
 * 
 * @author Administrator
 *
 */
public class CountBookAmount {

	@Test
	public void count() {
		int count = 0;
		List<SecondClass> secondClassList = SecondClassReader.getSecondClassList();
		for (SecondClass secondClass : secondClassList) {
			count += secondClass.getCount();
		}
		System.out.println(count);
	}

}
