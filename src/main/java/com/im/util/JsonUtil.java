package com.im.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * JSON工具类
 * 
 * @author Kevin-
 *
 */
public class JsonUtil {

	/**
	 * 将任意对象生成json字符串
	 * 
	 * @param object
	 *            任意对象
	 * @return json字符串
	 */
	public static String transformToJson(Object object) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(object);

		return jsonStr;
	}

	/**
	 * 将json字符串解析成指定类对象
	 * 
	 * @param jsonData
	 *            json字符串
	 * @param classOfT
	 *            类
	 * @return 指定的类对象
	 */
	public static <T> T getObjectByJson(String jsonData, Class<T> classOfT) {

		// 将json字符串解析成object对象
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(jsonData).getAsJsonObject();

		// 使用gson工具解析成指定的类对象（该对象的结果必须与json格式严格一致）
		Gson gson = new Gson();

		return gson.fromJson(object, classOfT);
	}

}
