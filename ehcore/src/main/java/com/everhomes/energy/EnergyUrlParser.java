package com.everhomes.energy;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/12/18.
 */
@Component
public class EnergyUrlParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        if(actionType == 13){
            Map<String,String> data= new Gson().fromJson(actionData,new TypeToken<HashMap<String,String>>(){}.getType());
            if(data!=null && data.size() > 0 && data.get("url")!=null){
                String wd = data.get("url").split("/")[3];
                if( wd.equals("energy-management") || wd.equals("energy-management")) res = 49100l;
            }
        }
        return res;
    }
}
