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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.banner.BannerProviderImpl;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.util.StringHelper;

@Component(AuthorizationModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX+"EhNamespaces"+1000000)
public class ZJAuthorizationModuleHandler implements AuthorizationModuleHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZJAuthorizationModuleHandler.class);
	   
	
	static final String url = "http://139.129.220.146:3578/openapi/Authenticate";
	
	static final String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";
	
	static final String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";

	@Override
	public PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd) {
		
		Map<String, String> params = generalParams(cmd);
		try {
			String jsonStr = HttpUtils.post(url, params, 10, "UTF-8");
			ZjgkJsonEntity<List<ZjgkResponse>> entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
			//请求成功，返回承租地址，那么创建家庭。
			if(entity.isSuccess()){
				createFamily(entity);
			}
			//创建工作流
			createWorkFlow(entity);
		} catch (Exception e) {
			LOGGER.error(""+e);
		}
		return null;
	}
	
	private void createFamily(ZjgkJsonEntity<List<ZjgkResponse>> entity) {
		// TODO Auto-generated method stub
		
	}

	private void createWorkFlow(ZjgkJsonEntity<List<ZjgkResponse>> entity) {
		
	}

	private Map<String, String> generalParams(PostGeneralFormCommand cmd){
		List<PostApprovalFormItem> values = cmd.getValues();
		Map<String, String> params= new HashMap<String,String>();
		for (PostApprovalFormItem item : values) {
			params.put(item.getFieldName(), item.getFieldValue());
		}
		params.put("appKey", appKey);
		params.put("timestamp", ""+System.currentTimeMillis());
		params.put("nonce", ""+(long)(Math.random()*100000));
		params.put("type", PERSONAL_AUTHORIZATION);
		String signature = computeSignature(params, secretKey);
		params.put("signature", signature);
		return params;
		
	}
	

	@Override
	public PostGeneralFormDTO organiztionAuthorization(PostGeneralFormCommand cmd) {
		return null;
	}

}
