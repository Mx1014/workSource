package com.everhomes.parking.yinxingzhijiexiaomao;

import java.util.TreeMap;

public class YinxingzhijieXiaomaoSignUtil {
	
	public static String  getSign(TreeMap<String, String> treeMap,String accessKeyValue) {
		
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("accessKeyValue=" + accessKeyValue);
		return YinxingzhijieXiaomaoMD5Util.MD5Encode(sb.toString(), "utf-8").toLowerCase();
	}
}
