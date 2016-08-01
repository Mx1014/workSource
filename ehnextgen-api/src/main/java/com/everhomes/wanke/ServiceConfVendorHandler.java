package com.everhomes.wanke;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.wanke.GetSignCommand;
import com.everhomes.rest.wanke.GetSignDTO;
import com.everhomes.rest.wanke.ListCommunityCommand;
import com.everhomes.rest.wanke.ListCommunityResponse;

public interface ServiceConfVendorHandler {
    String SERVICECONF_VENDOR_PREFIX = "ServiceConfVendor-";

    ListCommunityResponse loginAndGetCommunities(ListCommunityCommand cmd, HttpServletRequest req, HttpServletResponse resp);
    
    GetSignDTO getSign(GetSignCommand cmd);
}
