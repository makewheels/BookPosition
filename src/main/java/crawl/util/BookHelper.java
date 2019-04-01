package crawl.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import crawl.bean.Book;
import util.Constants;
import util.HttpUtil;

/**
 * 书的帮助类
 * 
 * @author Administrator
 *
 */
public class BookHelper {

	/**
	 * 通过recno查条码号
	 * 
	 * @param recno
	 * @return
	 */
	public static String requestHoldingJson(String recno) {
		return HttpUtil.get(Constants.BASE_URL + "/opac/api/holding/" + recno);
	}

	/**
	 * 更新holdingJson
	 * 
	 * @param book
	 */
	public static void updateHoldingJson(Book book) {
		String bookrecno = book.getBookrecno();
		String holdingJson = requestHoldingJson(bookrecno);
		book.setHoldingJson(holdingJson);
	}

	/**
	 * 根据传入book的holdingJson，解析出每一个条码号的json列表
	 * 
	 * @param book
	 * @return 如果没有条码号，则返回null
	 */
	public static List<String> getBarCodeList(Book book) {
		JsonObject jsonObject = new JsonParser().parse(book.getHoldingJson()).getAsJsonObject();
		JsonArray holdingList = jsonObject.get("holdingList").getAsJsonArray();
		if (holdingList.size() == 0) {
			return null;
		} else {
			List<String> barCodeJsonList = new ArrayList<>();
			for (int i = 0; i < holdingList.size(); i++) {
				String json = holdingList.get(i).toString();
				barCodeJsonList.add(json);
			}
			return barCodeJsonList;
		}
	}

	/**
	 * 根据条码号发请求，返回html
	 * 
	 * @param barCode
	 * @return
	 */
	public static String getPositionHtml(String barCode) {
		return HttpUtil.get(Constants.BASE_URL_INTERNAL_QUERY_LOCATION + barCode);
	}

	/**
	 * 从内网请求的html中，解析出图书定位信息
	 * 
	 * @param html
	 * @return
	 */
	public static String parsePositionFromHtml(String html) {
		return StringUtils.substringBetween(html, "var strWZxxxxxx = \"", "\";");
	}

}
