package com.everhomes.journal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.portal.PortalUrlParser;
import com.everhomes.rest.flow.FlowConstants;

/**
 * Created by Administrator on 2018/2/5.
 */
public class JournalParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        //{"url":"${home.url}/park-paper/index.html#/epaper_index#sign_suffix"}
        JSONObject json = JSONObject.parseObject(actionData);
        if (json != null && json.size() > 0) {
            String url = json.getString("url");
            String[] arrs = url.split("&");
            if (arrs[0].contains("park-paper")) {
                return FlowConstants.JOURNAL_MODULE;
            }
        }
        return null;
    }
}
