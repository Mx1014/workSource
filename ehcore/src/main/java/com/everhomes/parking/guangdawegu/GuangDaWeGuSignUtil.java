package com.everhomes.parking.guangdawegu;

import java.util.TreeMap;

public class GuangDaWeGuSignUtil {
	
	public static String  getSign(TreeMap<String, String> treeMap,String appkey) {
		
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("appKey=" + appkey);
		return GuangDaWeGuMD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
	}
}
