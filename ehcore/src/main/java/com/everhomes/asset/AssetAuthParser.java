//@formatter:off
package com.everhomes.asset;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wentian Wang on 2017/12/6.
 */

@Component
public class AssetAuthParser implements PortalUrlParser {
    private static Logger LOGGER = LoggerFactory.getLogger(AssetAuthParser.class);
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        Long res = null;
        try{
            if(actionType == 13){
                Map<String,String> data= new Gson().fromJson(actionData,new TypeToken<HashMap<String,String>>(){}.getType());
                if(data!=null && data.size() > 0 && data.get("url")!=null){
                    String[] wd = data.get("url").split("/");
                    if(wd == null || wd.length < 1) return res;
                    boolean containsAssetKeyWord = Arrays.stream(wd).anyMatch(s -> s.equals("property-payment") || s.equals("property-management"));
                    if(containsAssetKeyWord){
                        res = 20400l;
                    }
                }
            }
        }catch(Exception e){
            LOGGER.error("assetAuthParser failed, namespaceId = {}, actionData= {}, actionType = {}, itemLabel = {}"+e,
                    namespaceId,actionData,actionType,itemLabel);
        }
        return res;
    }
}
