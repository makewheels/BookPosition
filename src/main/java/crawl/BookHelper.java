package crawl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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
		try {
			String json = HttpUtil.get(Constants.BASE_URL_EXTERNAL + "/opac/api/holding/" + recno);
			JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
			JsonArray holdingList = jsonObject.get("holdingList").getAsJsonArray();
			if (holdingList.size() == 0) {
				return null;
			} else {
				JsonObject info = holdingList.get(0).getAsJsonObject();
				return info.get("barcode").getAsString();
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

}
