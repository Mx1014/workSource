package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.portal.PortalUrlParser;
import com.everhomes.rest.flow.FlowConstants;
import org.springframework.stereotype.Component;
import sun.util.resources.cldr.uk.CurrencyNames_uk;

/**
 * @author sw on 2017/12/14.
 */
@Component
public class PmtaskParser implements PortalUrlParser {

    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {

        //{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203531&displayName=物业报修"}

        JSONObject json = JSONObject.parseObject(actionData);
        if(json != null && json.size() > 0){
            String url = json.getString("url");
            String[] arrs = url.split("&");
            if (arrs[0].contains("propertyrepair")) {
                return FlowConstants.PM_TASK_MODULE;
            }
        }
        return null;
    }
}
