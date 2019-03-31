package crawl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	public static String getBarCodeByRecno(String recno) {
		String json = HttpUtil.get(Constants.BASE_URL_EXTERNAL + "/opac/api/holding/" + recno);
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
		JsonObject info = jsonObject.get("holdingList").getAsJsonArray().get(0).getAsJsonObject();
		return info.get("barcode").getAsString();
	}

}
