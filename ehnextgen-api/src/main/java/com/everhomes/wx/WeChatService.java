package com.everhomes.wx;

import com.everhomes.rest.wx.GetContentServerUrlCommand;
import com.everhomes.rest.wx.GetSignatureCommand;
import com.everhomes.rest.wx.GetSignatureResponse;

public interface WeChatService {

	String getAccessToken();
	
	String getJsapiTicket();
	
	GetSignatureResponse getSignature(GetSignatureCommand cmd);
	
	String getContentServerUrl(GetContentServerUrlCommand cmd);

	String getAppIdByNamespaceId(Integer namespaceId);
}
