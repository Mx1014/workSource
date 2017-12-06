//@formatter:off
package com.everhomes.asset;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wentian Wang on 2017/12/6.
 */

public class AssetAuthParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        if(actionType == 13){
            Map<String,String> data= new Gson().fromJson(actionData,new TypeToken<HashMap<String,String>>(){}.getType());
            if(data!=null && data.size() > 0 && data.get("url")!=null){
                String wd = data.get("url").split("/")[3];
                if( wd.equals("property-payment") || wd.equals("property-management")) return 20400l;
            }
        }
    }
}
