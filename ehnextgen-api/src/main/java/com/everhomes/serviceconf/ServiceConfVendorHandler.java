package com.everhomes.serviceconf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.serviceconf.ListCommunityCommand;
import com.everhomes.rest.serviceconf.ListCommunityResponse;

public interface ServiceConfVendorHandler {
    String SERVICECONF_VENDOR_PREFIX = "ServiceConfVendor-";

    ListCommunityResponse loginAndGetCommunities(ListCommunityCommand cmd, HttpServletRequest req, HttpServletResponse resp);
}
