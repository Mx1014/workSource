package com.everhomes.serviceconf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.serviceconf.ListCommunityCommand;
import com.everhomes.rest.serviceconf.ListCommunityResponse;
import com.everhomes.rest.serviceconf.ListCommunityServiceCommand;
import com.everhomes.rest.serviceconf.ListCommunityServiceResponse;

public interface ServiceConfService {
	ListCommunityServiceResponse listCommunityServices(ListCommunityServiceCommand cmd);
	
	ListCommunityResponse loginAndGetCommunities(ListCommunityCommand cmd, HttpServletRequest req, HttpServletResponse resp);
}
