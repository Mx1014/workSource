package com.everhomes.parking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ketuo.EncryptUtil;
import com.everhomes.util.RuntimeErrorException;

public class ChunengTest {

	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	private static final String RECHARGE = "/api/pay/CardRecharge";
	private static final String GET_CARD = "/api/pay/GetCarCardInfo";
	private static final String GET_TYPES = "/api/pay/GetCarTypeList";
	private static final String GET_CARd_RULE = "/api/pay/GetCardRule";
	private static final String GET_TEMP_FEE = "/api/pay/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/pay/PayParkingFee";
	private static final String ADD_MONTH_CARD = "/api/card/AddMonthCarCardNo_KX";
	private static final String RULE_TYPE = "1"; //只显示ruleType = 1时的充值项
	

	
	public static void main(String[] args) {
		JSONObject param = new JSONObject();
		
		param.put("plateNo", "B22222");
//		param.put("money", "30000");
////		param.put("carType", "2");
//		param.put("plateNo", "B12347");
//		B5720Z
//		BX082N
//		B7DC77

		String json = post(param, GET_CARD);
        System.out.println(json);
        System.out.println(new Date(1490975999000L));
//        JSONObject param2 = new JSONObject();
//        param2.put("cardId", "528");
//        param2.put("ruleType", "1");
//        param2.put("ruleAmount", "1");
//        param2.put("payMoney", "50000");
//        param2.put("startTime", "2016-10-01 00:00:00");
//        param2.put("endTime", "2017-01-31 23:59:59");
//        param2.put("freeMoney", "20000");
//        
//        String json2 = Test.post(param2, RECHARGE);
//        System.out.println(json2);
	}
	
	
	
	
	
	public static String post(JSONObject param, String type) {
//		http://183.238.146.34:8099
//		user:  ktapi
//		pwd:  0106E5
//		key:  BD8F1F46E589D97E3DEEECF4
		HttpPost httpPost = new HttpPost("http://218.17.157.47:8099/" + type);
		StringBuilder result = new StringBuilder();
		
        String key = "BD8F1F46E589D97E3DEEECF4";
        String iv = "20170227";
        String user = "ktapi";
        String pwd = "0106E5";
        String data = null;
		try {
			data = EncryptUtil.getEncString(param, key, iv);
		} catch (Exception e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking encrypt param error.");
		}
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("data", data));
		CloseableHttpResponse response = null;
		InputStream instream = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));
			httpPost.addHeader("user", user);
			httpPost.addHeader("pwd", pwd);
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = null;
				reader = new BufferedReader(new InputStreamReader(instream,"utf8"));
				String s;
            	
            	while((s = reader.readLine()) != null){
            		result.append(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
            try {
            	instream.close();
				response.close();
			} catch (IOException e) {
			}
        }
		String json = result.toString();
		
		return json;
	}
	
	/**
	 * 获取加密后的字符串
	 * @return
	 */
	private static String stringMD5(String pw) {
		try {  
			// 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）  
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");  
			// 输入的字符串转换成字节数组  
			byte[] inputByteArray = pw.getBytes();  
			// inputByteArray是输入字符串转换得到的字节数组  
			messageDigest.update(inputByteArray);  
			// 转换并返回结果，也是字节数组，包含16个元素  
			byte[] resultByteArray = messageDigest.digest();  
			// 字符数组转换成字符串返回  
			return byteArrayToHex(resultByteArray);  
		} catch (NoSuchAlgorithmException e) {  
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Xianluobo encrypt error");
		}  
	}

	private static String byteArrayToHex(byte[] byteArray) {  
       
		// 首先初始化一个字符数组，用来存放每个16进制字符  
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
		char[] resultCharArray =new char[byteArray.length * 2];  
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  
		int index = 0; 
		for (byte b : byteArray) {  
			resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
			resultCharArray[index++] = hexDigits[b& 0xf];  
		}
		// 字符数组组合成字符串返回  
		return new String(resultCharArray);  
	}
}
