package com.everhomes.address;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;

public class computeSignatureDemo {
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
	
	static void test(){
		Map<String, String> params = new HashMap<>();
//		List<Long> userIds = new ArrayList<Long>();
//		userIds.add(111L);
//		userIds.add(222L);
		params.put("appKey","b5afc319-13ab-4662-b0a5-2eaa0f0526d1");
		params.put("nonce", "d0448ebc1dc842bf9a53a49ccd9b9426");
		params.put("timestamp", "1533630355");
//		params.put("MacAddress", "DE:85:D2:31:3E:00");
//		params.put("userIds[0]", "10");
//		params.put("userIds[1]", "222");
		params.put("phone", "17727424695");
		params.put("MacAddress", "DE:85:D2:31:3E:00");
		params.put("validEndMs", "1528182986478");
		System.out.println(computeSignature(params,"VPC5XqFWYtRShigIpXU7tiPWix2zGP6+hpcdet2G7iG967nc7KxBI2u/0VggLKct9UrDvttu+Lw9IKzKmdwNug=="));
	}
	
	public static void main(String[] args) {
		test();
	}
}
