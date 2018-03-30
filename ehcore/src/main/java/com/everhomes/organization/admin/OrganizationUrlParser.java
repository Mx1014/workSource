package com.everhomes.organization.admin;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ying.xiong on 2018/3/13.
 */
@Component
public class OrganizationUrlParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        if(actionType == 13 && "organization".equals(actionData)){
            res = 33000l;
        }
        return res;
    }
}
