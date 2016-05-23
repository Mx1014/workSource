package com.everhomes.test.core;

import com.everhomes.util.GsonJacksonDateAdapter;
import com.everhomes.util.GsonJacksonTimestampAdapter;
import com.everhomes.util.ReflectionHelper;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonHelper {
	/**
	 * 把JSON字符串格式化为可读性强的方式
	 * @param uglyJSONString
	 * @return
	 */
	public static String formatJsonString(String uglyJSONString) {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        return gson.toJson(je);
	}
	
	public static void test(String filePath) {
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(filePath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
