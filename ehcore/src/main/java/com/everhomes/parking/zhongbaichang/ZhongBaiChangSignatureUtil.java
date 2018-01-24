// @formatter:off
package com.everhomes.parking.zhongbaichang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhongBaiChangSignatureUtil {

	/**
	 * 功能：生成签名结果
	 * 
	 * @paramsArray要签名的数组
	 * @param key
	 *            安全校验码
	 * @return签名结果字符串
	 */
	public static String getSign(Map<String, Object> sArray) {
		Map<String, Object> tempMap = paraFilter(sArray);
		String prestr = createLinkString(tempMap); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		// String signature = Md5Encrypt.md5(prestr);
		String signature = ZhongBaiChangMd5Util.MD5(prestr);
		return signature;
	}
	
	/**
	 * 功能：生成签名结果
	 * 
	 * @paramsArray要签名的数组
	 * @param key
	 *            安全校验码
	 * @return签名结果字符串
	 */
	public static String getSign(Map<String, Object> sArray, String key) {
		Map<String, Object> tempMap = paraFilter(sArray);
		String prestr = createLinkString(tempMap); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		prestr = prestr + key; // 把拼接后的字符串再与安全校验码直接连接起来(key 双方确定好的秘钥)
		// String signature = Md5Encrypt.md5(prestr);
		String signature = ZhongBaiChangMd5Util.MD5(prestr);
		return signature;
	}

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @paramsArray签名参数组
	 * @return去掉空值与签名参数后的新签名参数组
	 */
	private static Map<String, Object> paraFilter(Map<String, Object> sArray) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = null == sArray.get(key) ? null : sArray.get(key)
					.toString();
			if (value == null || value.equals("")
					|| key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")
					|| key.equalsIgnoreCase("signature")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @paramparams需要排序并参与字符拼接的参数组
	 * @return拼接后字符串
	 */
	private static String createLinkString(Map<String, Object> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key).toString();

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

}
