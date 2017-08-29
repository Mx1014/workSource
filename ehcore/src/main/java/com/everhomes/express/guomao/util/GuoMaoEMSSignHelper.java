// @formatter:off
package com.everhomes.express.guomao.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

public class GuoMaoEMSSignHelper {
	public static String sha256Sign(Map<String, Object> params, String appSecret, String charset) throws RuntimeErrorException{
		// 得到“生成签名的字符串”
		String content = getSortParams(params) + appSecret;
		// 对“生成签名的字符串”进行MD5，并进行BASE64操作，根据请求编码得到签名
		if (charset == null || "".equals(charset)) {
			charset = "";
		}
		String sign = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA-256");
			sign = Base64.getEncoder().encodeToString(md5.digest(content.getBytes(charset)));
		} catch (UnsupportedEncodingException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"guomao ems sign excetion = "+e);
		
		} catch (NoSuchAlgorithmException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"guomao ems sign excetion = "+e);
		}
		return sign;
	}
	
	public static String getSortParams(Map<String, Object> params) {
		// 删掉sign参数
		params.remove("sign");
		String contnt = "";
		Set<String> keySet = params.keySet();
		List<String> keyList = new ArrayList<String>();
		for (String key : keySet) {
			Object value = params.get(key);
			// 将值为空的参数排除
			if (value != null && value.toString().length() > 0) {
				keyList.add(key);
			}
		}
		sort(keyList);
		// 将参数和参数值按照排序顺序拼装成字符串
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			Object value = params.get(key);
			if(value instanceof Map){
				contnt += key + StringHelper.toJsonString(value);
			}else{
				contnt += key + value;
			}
		}
		return contnt;
	}
	
	public static void sort(List<String> keyList){
		Collections.sort(keyList, (o1,o2)->{
			int length = Math.min(o1.length(), o2.length());
			for (int i = 0; i < length; i++) {
				char c1 = o1.charAt(i);
				char c2 = o2.charAt(i);
				int r = c1 - c2;
				if (r != 0) {
					// char值小的排前边
					return r;
				}
			}
			// 2个字符串关系是str1.startsWith(str2)==true
			// 取str2排前边
			return o1.length() - o2.length();
		});
	}
}
