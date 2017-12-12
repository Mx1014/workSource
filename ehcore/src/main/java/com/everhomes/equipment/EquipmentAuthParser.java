//@formatter:off
package com.everhomes.equipment;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rui.jia  2017/12/12 16 :36
 */

@Component
public class EquipmentAuthParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        if (actionType == 13) {
            Map<String, String> data = new Gson().fromJson(actionData, new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (data != null && data.size() > 0 && data.get("url") != null) {
                String url = data.get("url");
                if (url.contains("equipment-inspection"))
                    res = 20080L;
            }
        }
        return res;
    }
}
