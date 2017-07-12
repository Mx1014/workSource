package com.everhomes.authorization;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.collections.CollectionUtils;

import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

/**
 * 
 */
public interface AuthorizationModuleHandler {

    String GENERAL_FORM_MODULE_HANDLER_PREFIX = "AuthorizationModuleHandler-";
    
    String PERSONAL_AUTHORIZATION = "1";
    String ORGANIZATION_AUTHORIZATION = "2";
    
    PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd);
    
    PostGeneralFormDTO organiztionAuthorization(PostGeneralFormCommand cmd);
    /**
	 * 加密参数算法
	 */
	default String computeSignature(Map<String, String> params, String secretKey) {
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
	
	/**
	 *  参数验证的方法
	 */
	default boolean verifySignature(Map<String, String> params, String secretKey, String signatureToVerify) {
	    String signature = computeSignature(params, secretKey);
	    if(signature.equals(signatureToVerify))
	        return true;
	    return false;
	}
}
