package com.everhomes.quality;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rui.jia  2017/12/15 18 :52
 */

public class QualityAuthParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long moduleId = null;
        if (actionType == 44) {
            Map<String, String> data = new Gson().fromJson(actionData, new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (data != null && data.size() > 0 && data.get("entryUrl") != null) {
                String entryUrl = data.get("entryUrl");
                if (entryUrl.contains("quality")) {
                    moduleId = QualityConstant.QUALITY_MODULE;
                }
            }

        }
        return moduleId;
    }
}
