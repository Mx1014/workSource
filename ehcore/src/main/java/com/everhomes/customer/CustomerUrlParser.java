package com.everhomes.customer;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/12/14.
 */
public class CustomerUrlParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        if(actionType == 13 && "customer".equals(actionData)){
            res = 21100l;
        }
        return res;
    }
}
