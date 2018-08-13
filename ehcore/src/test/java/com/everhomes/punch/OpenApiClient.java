package com.everhomes.punch;
 

 
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.everhomes.util.SignatureHelper;

public class OpenApiClient {
	public static void main(String[] args){
		String result = null;
		try {
			 
			String url =   "https://parktest.szbay.com/evh/openapi/getOrgCheckInData";
//			String url =   "http://localhost:8080/evh/openapi/getOrgCheckInData";
			CloseableHttpClient closeableHttpClient = createHttpsClient();
			// 建立HttpPost对象
			HttpPost httppost = new HttpPost(url);  
			Map<String,String> map = new HashMap<String, String>(); 
			map.put("endDate", "1528992000000");
			map.put("beginDate",   "1527782400000");
			//1045660
			map.put("orgId", "1035830");
			//93e8275c-31e2-11e5-b7ad-b083fe4e159f
			map.put("appKey", "d06d0ba1-49a6-411f-a153-8d3c08fdeb08");
			map.put("timestamp", new Date().getTime() + "");
			map.put("nonce",  "23432423");

			String signature = SignatureHelper.computeSignature(map,  "d1l5CAZl76/zjStIA+WXN8lmBAus/BQQcmxvxC6ypQdNMhlNbYCvf9mEL0z6KMZU8IyiaJSNc2ijB3+fol+vvQ=");
//			String signature = SignatureHelper.computeSignature(map,  "2nDpmzJj63Un0GzXyeZKUKlVSOKzNHv4FidFL9uCpNaLq6rqE0VAOv3uPaR0jWIRMNqedgci3vzLPAkaX1jg6Q==");
			map.put("signature", signature);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                    formparams, Consts.UTF_8);
			httppost.setEntity(entity); 
			HttpResponse httpResponse = closeableHttpClient.execute(httppost);
			 
			HttpEntity httpEntity1 = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity1);
			// 关闭连接
			closeableHttpClient.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		System.out.print(result);
	} 

	public static CloseableHttpClient createHttpsClient() throws Exception {
		X509TrustManager x509mgr = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] xcs, String string) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] xcs, String string) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { x509mgr },
				new java.security.SecureRandom());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	
	public static String computeSignature(Map<String, String> params, String secretKey) {
	    assert(params != null);
	    assert(secretKey != null);
	    
	    try {
	        Mac mac = Mac.getInstance("HmacSHA1");
	        byte[] rawKey = Base64.decodeBase64(secretKey);
	        
	        SecretKeySpec keySpec = new SecretKeySpec(rawKey, "HmacSHA1");
	        mac.init(keySpec);
	       
	        List<String> keyList = new ArrayList<String>();
	        CollectionUtils.addAll(keyList, params.keySet().iterator());
	        Collections.sort(keyList);
	        
	        for(String key : keyList) {
	            mac.update(key.getBytes("UTF-8"));
	            String val = params.get(key);
	            if(val != null && !val.isEmpty())
	                mac.update(val.getBytes("UTF-8"));
	        }
	        
	        byte[] encryptedBytes = mac.doFinal();
	        String signature = Base64.encodeBase64String(encryptedBytes);
	        
	        return signature;
	    } catch(InvalidKeyException e) {
	        throw new InvalidParameterException("Invalid secretKey for signing");
	    } catch(NoSuchAlgorithmException e) {
	        throw new RuntimeException("NoSuchAlgorithmException for HmacSHA1", e);
	    } catch(UnsupportedEncodingException e) {
	        throw new RuntimeException("UnsupportedEncodingException for UTF-8", e);
	    }
	}

	public static boolean verifySignature(Map<String, String> params, String secretKey, String signatureToVerify) {
	    String signature = computeSignature(params, secretKey);
	    
	    if(signature.equals(signatureToVerify))
	        return true;
	    
	    return false;
	}
	
}
