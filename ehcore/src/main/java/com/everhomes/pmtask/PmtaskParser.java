package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.portal.PortalUrlParser;
import com.everhomes.rest.flow.FlowConstants;
import org.springframework.stereotype.Component;

/**
 * @author sw on 2017/12/14.
 */
@Component
public class PmtaskParser implements PortalUrlParser {

    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {

        //{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203531&displayName=物业报修"}

        JSONObject json = JSONObject.parseObject(actionData);
        String url = json.getString("url");
        String[] arrs = url.split("&");

//        for (String s: arrs) {
//            int spe = s.indexOf("=");
//            String key = s.substring(0, spe);
//            String value = s.substring(spe + 1);
//            if ("taskCategoryId".equals(key)) {
//                return value;
//            }
//        }

        if (arrs[0].contains("propertyrepair")) {
            return FlowConstants.PM_TASK_MODULE;
        }
        return null;
    }
}
