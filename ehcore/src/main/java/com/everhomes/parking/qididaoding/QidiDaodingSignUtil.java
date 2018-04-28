package com.everhomes.parking.qididaoding;

import java.util.TreeMap;

public class QidiDaodingSignUtil {
	
	public static String  getSign(TreeMap<String, String> treeMap,String signkey) {
		
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(String.valueOf(treeMap.get(key))).append("&");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(signkey);
		return QidiDaodingMD5Util.MD5Encode(sb.toString(), "utf-8").toLowerCase();
	}
}
