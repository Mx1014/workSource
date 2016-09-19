package com.everhomes.user;

import java.util.List;

import com.everhomes.rest.user.RequestFieldDTO;


public interface CustomRequestHandler {

	String CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX = "CustomRequest-";

	void addCustomRequest(String json);
	
	List<RequestFieldDTO> getCustomRequestInfo(Long id);
}
