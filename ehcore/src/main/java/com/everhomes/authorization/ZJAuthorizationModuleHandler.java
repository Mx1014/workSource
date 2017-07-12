package com.everhomes.authorization;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.banner.BannerProviderImpl;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.util.StringHelper;

@Component(AuthorizationModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX+"EhNamespaces"+1000000)
public class ZJAuthorizationModuleHandler implements AuthorizationModuleHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZJAuthorizationModuleHandler.class);
	   
	
	String url = "http://139.129.220.146:3578/openapi/Authenticate";
	
	String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";
	
	String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";

	@Override
	public PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd) {
		List<PostApprovalFormItem> values = cmd.getValues();
		Map<String, String> params= new HashMap<String,String>();
		for (PostApprovalFormItem item : values) {
			params.put(item.getFieldName(), item.getFieldValue());
		}
		params.put("appKey", appKey);
		params.put("timestamp", ""+System.currentTimeMillis());
		params.put("nonce", ""+(long)(Math.random()*10000));
//		params.put("crypto", "");
		params.put("type", "1");
		String signature = computeSignature(params, secretKey);
		params.put("signature", signature);
		try {
			String jsonStr = HttpUtils.post(url, params, 10, "UTF-8");
			LOGGER.info(StringHelper.toJsonString(params));
			LOGGER.info(jsonStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String computeSignature(Map<String, String> params, String secretKey) {
	    assert(params != null);
	    assert(secretKey != null);
	    
	    try {
	        Mac mac = Mac.getInstance("HmacSHA1");
	        byte[] rawKey = Base64.getDecoder().decode(secretKey);
	        
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
	        String signature = Base64.getEncoder().encodeToString(encryptedBytes);
	        
	        return signature;
	    } catch(InvalidKeyException e) {
	        throw new InvalidParameterException("Invalid secretKey for signing");
	    } catch(NoSuchAlgorithmException e) {
	        throw new RuntimeException("NoSuchAlgorithmException for HmacSHA1", e);
	    } catch(UnsupportedEncodingException e) {
	        throw new RuntimeException("UnsupportedEncodingException for UTF-8", e);
	    }
	}
	

	@Override
	public PostGeneralFormDTO organiztionAuthorization(PostGeneralFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
