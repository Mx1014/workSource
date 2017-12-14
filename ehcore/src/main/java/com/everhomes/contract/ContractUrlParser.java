package com.everhomes.contract;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/12/14.
 */
public class ContractUrlParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        if(actionType == 13 && "contract".equals(actionData)){
            res = 21200l;
        }
        return res;
    }
}
