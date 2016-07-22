package com.everhomes.wanke;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.wanke.ListCommunityCommand;
import com.everhomes.rest.wanke.ListCommunityResponse;
import com.everhomes.rest.wanke.ListCommunityServiceCommand;
import com.everhomes.rest.wanke.ListCommunityServiceResponse;

public interface ServiceConfService {
	ListCommunityServiceResponse listCommunityServices(ListCommunityServiceCommand cmd);
	
	ListCommunityResponse loginAndGetCommunities(ListCommunityCommand cmd, HttpServletRequest req, HttpServletResponse resp);
}
