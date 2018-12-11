package com.everhomes.xfyun;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.rest.xfyun.AiUiIntentDTO;
import com.everhomes.rest.xfyun.AiUiIntentSemanticDTO;
import com.everhomes.aclink.uclbrt.BASE64Decoder;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.xfyun.AfterDealRespMsg;
import com.everhomes.rest.xfyun.AfterDealRespUserParams;
import com.everhomes.rest.xfyun.AfterDealResponse;

public class XunfeiYunAfterDealTool {
	
	private AfterDealResponse afterDealResponse;
	private AfterDealRespUserParams afterDealRespUserParams;
	private AfterDealRespMsg afterDealRespMsg;
	private AiUiIntentDTO aiUiIntent;
	
	private String verifiedText;
	
	public XunfeiYunAfterDealTool() {
		
	}

	public XunfeiYunAfterDealTool(String jsonStr) {
		if (StringUtils.isBlank(jsonStr)) {
			return;
		}
		
		afterDealResponse = JSONObject.parseObject(jsonStr, AfterDealResponse.class);
		if (null == afterDealResponse) {
			return ;
		}
		
		String userParams = decode(afterDealResponse.getUserParams());
		if (null != userParams) {
			afterDealRespUserParams = JSONObject.parseObject(userParams, AfterDealRespUserParams.class);
		}
		
		afterDealRespMsg = JSONObject.parseObject(afterDealResponse.getMsg(), AfterDealRespMsg.class);
		
		verifyText();
	}
	
	
	public static String decode(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	private void verifyText() {
		String contentStr = decode(afterDealRespMsg.getContent());
		JSONObject json = JSONObject.parseObject(contentStr);
		aiUiIntent = JSONObject.parseObject(json.getString("intent"), AiUiIntentDTO.class);
		
		List<AiUiIntentSemanticDTO> dtos = aiUiIntent.getSemantic();
		if (!CollectionUtils.isEmpty(dtos)) {
			aiUiIntent.setFirstIntent(dtos.get(0).getIntent());
		}
		verifiedText = aiUiIntent.getText();
	}

	public boolean isSuccess() {

		if (null == afterDealResponse || afterDealResponse.isEmpty()) {
			
			return false;
			
		}
		
		return true;
	}
	
	public String getLoginToken() {
		return null == afterDealRespUserParams ? null : afterDealRespUserParams.getToken();
	}

	public boolean isVerified() {
		if (null != aiUiIntent && null != aiUiIntent.getRc() && 0 == aiUiIntent.getRc()) {
			return true;
		}
		
		return false;
		
	}

	public RouterQueryParamCommand getRouterQueryParams() {
		
		RouterQueryParamCommand cmd = new RouterQueryParamCommand();
		cmd.setVerifyText(verifiedText);
		if (null != afterDealRespUserParams && null != afterDealRespUserParams.getContext()) {
			cmd.setContext(afterDealRespUserParams.getContext());
		} 
		
		if (null != aiUiIntent) {
			cmd.setIntent(aiUiIntent);
		}
		
		return cmd;
	}

}
