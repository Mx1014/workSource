package com.everhomes.wx;

import com.everhomes.rest.wx.GetContentServerUriCommand;
import com.everhomes.rest.wx.GetContentServerUrlCommand;
import com.everhomes.rest.wx.GetSignatureCommand;
import com.everhomes.rest.wx.GetSignatureResponse;

public interface WeChatService {

	String getAccessToken();
	
	String getJsapiTicket();
	
	GetSignatureResponse getSignature(GetSignatureCommand cmd);
	
	String getContentServerUrl(GetContentServerUrlCommand cmd);

	String getContentServerUri(GetContentServerUriCommand cmd);

//    String testRestCall(String cmd, Object params, String url);

    String getAppIdByNamespaceId(Integer namespaceId);
}
