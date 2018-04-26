package com.everhomes.parking.kexinxiaomao;

import java.util.TreeMap;

public class KexinXiaomaoSignUtil {
	
	public static String  getSign(TreeMap<String, String> treeMap,String accessKeyValue) {
		
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("accessKeyValue=" + accessKeyValue);
		return KexinXiaomaoMD5Util.MD5Encode(sb.toString(), "utf-8").toLowerCase();
	}
}
