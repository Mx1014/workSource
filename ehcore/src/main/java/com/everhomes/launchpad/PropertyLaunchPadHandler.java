// @formatter:off
package com.everhomes.launchpad;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.category.CategoryConstants;
import com.everhomes.category.CategoryType;


@Component(LaunchPadHandler.LAUNCH_PAD_ITEM_RESOLVER_PREFIX + LaunchPadConstants.GAACTIONS)
public class PropertyLaunchPadHandler implements LaunchPadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLaunchPadHandler.class);
    @Autowired
    private LaunchPadProvider launchPadProvider;
    @Override
    public LaunchPadItem accesProcessLaunchPadItem(String userToken, long commnunityId, LaunchPadItem launchPadItem) {

        assert(launchPadItem != null);
        launchPadItem.setActionData(parserJson(userToken,commnunityId,launchPadItem));
        
        return launchPadItem;
    }
    
    @SuppressWarnings("unchecked")
    private String parserJson(String userToken, long commnunityId,LaunchPadItem launchPadItem) {
        JSONObject jsonObject = new JSONObject();
        try{
            if(launchPadItem.getItemGroup().equals(ItemGroup.GAACTIONS)){
                String itemName = launchPadItem.getItemName();
                if(itemName.equals(CategoryType.ADVISE.getCode())){
                    jsonObject.put(LaunchPadConstants.CATEGORY_ID, CategoryConstants.CATEGORY_ID_GA_ADVISE);
                }else if(itemName.equals(CategoryType.HELP.getCode())){
                    jsonObject.put(LaunchPadConstants.CATEGORY_ID, CategoryConstants.CATEGORY_ID_GA_HELP);
                }
                else if(itemName.equals(CategoryType.NOTICE.getCode())){
                    jsonObject.put(LaunchPadConstants.CATEGORY_ID, CategoryConstants.CATEGORY_ID_GA_NOTICE);
                }
                else if(itemName.equals(CategoryType.REPAIR.getCode())){
                    jsonObject.put(LaunchPadConstants.CATEGORY_ID, CategoryConstants.CATEGORY_ID_GA_REPAIR);
                }
            }
            jsonObject.put(LaunchPadConstants.COMMUNITY_ID, commnunityId);
        }catch(Exception e){
            LOGGER.error("Parser json is error,userToken=" + userToken + ",communityId=" + commnunityId,e.getMessage());
        }
        
        return jsonObject.toJSONString();
    }

   
}
