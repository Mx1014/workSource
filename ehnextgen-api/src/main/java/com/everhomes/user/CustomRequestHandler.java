package com.everhomes.user;

import java.util.List;

import com.everhomes.rest.user.AddRequestCommand;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;


public interface CustomRequestHandler {

	String CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX = "CustomRequest-";

	void addCustomRequest(AddRequestCommand cmd);
	
	GetRequestInfoResponse getCustomRequestInfo(Long id);
}
