package com.everhomes.openapi;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.PreOrderDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by chongxin.yang on 2018/4/12.
 */
public interface BusinessOpenVendorHandler {
    String BUSINESSOPEN_VENDOR_PREFIX = "BusinessOpenVendor-";

    List<Long> getUserId(String customJson, Integer namespaceId);
    
}
