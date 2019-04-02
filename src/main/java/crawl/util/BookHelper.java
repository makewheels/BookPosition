package crawl.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import crawl.bean.BarCode;
import crawl.bean.BarCodeDetail;
import crawl.bean.Book;
import crawl.bean.Position;
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

	// 大庆市图书馆的orglocal数组
	private static String[] daqingLibraryOrglocalArray = { "WX1", "WX2", "SETSG", "24TH" };

	/**
	 * 判断barCode是不是大庆市图书馆的书
	 * 
	 * @param barCode
	 * @return
	 */
	public static boolean isDaqingLibarayBarCode(BarCode barCode) {
		BarCodeDetail barCodeDetail = JSON.parseObject(barCode.getJson(), BarCodeDetail.class);
		String orglocal = barCodeDetail.getOrglocal();
		for (String each : daqingLibraryOrglocalArray) {
			if (each.equals(orglocal)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从爬虫请求得到的变量中，解析出position对象
	 * 
	 * @param position
	 * @return
	 */
	public static Position parsePositionFromString(String position) {
		if (StringUtils.isEmpty(position)) {
			return null;
		}
		String[] split1 = position.split("\\|");
		String code = split1[0];
		String[] split2 = split1[1].split(" ");
		String belong = split2[0];
		String detail = split2[1];
		return new Position(code, belong, detail);
	}
}
