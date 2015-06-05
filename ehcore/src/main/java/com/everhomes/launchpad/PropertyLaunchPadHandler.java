// @formatter:off
package com.everhomes.launchpad;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.category.CategoryConstants;
import com.everhomes.category.CategoryType;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + AppConstants.APPID_PM)
public class PropertyLaunchPadHandler implements LaunchPadHandler {
    
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(long userId, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setJsonObj(parserJson(userId,commnunityId,launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(long userId, long commnunityId,LaunchPadItem launchPadItem) {
        JSONObject jsonObject = new JSONObject();
        String itemName = launchPadItem.getItemName();
        if(itemName.equals(CategoryType.ADVISE.getCode())){
            jsonObject.put("categoryId", CategoryConstants.CATEGORY_ID_GA_ADVISE);
        }else if(itemName.equals(CategoryType.HELP.getCode())){
            jsonObject.put("categoryId", CategoryConstants.CATEGORY_ID_GA_HELP);
        }
        else if(itemName.equals(CategoryType.NOTICE.getCode())){
            jsonObject.put("categoryId", CategoryConstants.CATEGORY_ID_GA_NOTICE);
        }
        else if(itemName.equals(CategoryType.REPAIR.getCode())){
            jsonObject.put("categoryId", CategoryConstants.CATEGORY_ID_GA_REPAIR);
        }
        jsonObject.put("commnunityId", commnunityId);
        return jsonObject.toJSONString();
    }

   
}
