//@formatter:off
package com.everhomes.asset;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.asset.AssetInstanceConfigDTO;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.servicemoduleapp.CreateAnAppMappingCommand;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Wentian Wang on 2018/5/24.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.ASSET_MODULE)
public class AssetPortalPublishHandler implements PortalPublishHandler{
     private Logger LOGGER = LoggerFactory.getLogger(AssetPortalPublishHandler.class);

    @Autowired
    private AssetService assetService;
    
    @Autowired
    private ServiceModuleAppService serviceModuleAppService;
    
    // zhang jiang gao ke holds a different uri because in this namespace, the older asset UI is still in use
   @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        if(instanceConfig == null || !instanceConfig.contains("categoryId")){
            // new pushlish app
            JSONObject config = new JSONObject();
            String urlKey = "url";
            StringBuilder urlValue = new StringBuilder();
            if(namespaceId == 999971){
                urlValue.append("${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix");
            }else {
                urlValue.append("${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix");
            }
            config.put(urlKey, urlValue.toString());
            // get categoryId from asset_category
            long categoryId = assetService.getNextCategoryId(namespaceId, UserContext.currentUserId(), instanceConfig);
            config.put("categoryId", categoryId);
            String ret = config.toJSONString();
            assetService.saveInstanceConfig(categoryId, ret);
            return ret;
        }
        return instanceConfig;

    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig) {
        if(instanceConfig == null) return null;
        try{
            JSONObject obj = (JSONObject)new JSONParser().parse(instanceConfig);
            return String.valueOf(obj.get("categoryId"));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
    	try {
            JSONObject ret = new JSONObject();
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(instanceConfig);
            String url = (String)jsonObject.get("url");
            String categoryId = "";
            try{
                Long temp = (Long)jsonObject.get("categoryId");
                categoryId = String.valueOf(temp);
            }catch (Exception e){
                categoryId = (String)jsonObject.get("categoryId");
            }
            StringBuilder sb = new StringBuilder();
            if(url != null) {
            	String[] split = url.split("\\?");
                if(split.length != 2){
                    return null;
               }
               sb.append(split[0] + "?" + "categoryId=" + categoryId + "&" + split[1]);
            }
           ret.put("url", sb.toString());
           return ret.toJSONString();
        } catch (Exception e) {
            LOGGER.error("failed to getItemActionData in AssetPortalHandler, instanceConfig is={}", instanceConfig, e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData){
       return null;
    }
    
    public void afterAllAppPulish(ServiceModuleApp app){
    	String instanceConfig = app.getInstanceConfig();
    	try {
    		if(instanceConfig != null && instanceConfig != "") {
    			//格式化instanceConfig的json成对象
    			AssetInstanceConfigDTO assetInstanceConfigDTO = (AssetInstanceConfigDTO) StringHelper.fromJsonString(instanceConfig, AssetInstanceConfigDTO.class);
    			if(assetInstanceConfigDTO != null && assetInstanceConfigDTO.getContractOriginId() != null) {
    				Long originId = assetInstanceConfigDTO.getContractOriginId();
    				ServiceModuleApp contractApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(originId);
    				if(contractApp != null) {
    					AssetInstanceConfigDTO contractInstanceConfigDTO = 
    							(AssetInstanceConfigDTO) StringHelper.fromJsonString(contractApp.getInstanceConfig(), AssetInstanceConfigDTO.class);
    					CreateAnAppMappingCommand cmd = new CreateAnAppMappingCommand();
    					cmd.setAssetCategoryId(assetInstanceConfigDTO.getCategoryId());
    					cmd.setContractCategoryId(contractInstanceConfigDTO.getCategoryId());
    					cmd.setContractChangeFlag(assetInstanceConfigDTO.getContractChangeFlag());
    					cmd.setContractOriginId(assetInstanceConfigDTO.getContractOriginId());
    					cmd.setEnergyFlag(assetInstanceConfigDTO.getEnergyFlag());
    					cmd.setNamespaceId(app.getNamespaceId());
    					assetService.createOrUpdateAnAppMapping(cmd);
    				}else {
    					CreateAnAppMappingCommand cmd = new CreateAnAppMappingCommand();
    					cmd.setAssetCategoryId(assetInstanceConfigDTO.getCategoryId());
    					cmd.setEnergyFlag(assetInstanceConfigDTO.getEnergyFlag());
    					cmd.setNamespaceId(app.getNamespaceId());
    					assetService.createOrUpdateAnAppMapping(cmd);
    				}
    			}
    		}
    	}catch (Exception e) {
            LOGGER.error("failed to afterAllAppPulish in AssetPortalHandler, instanceConfig is={}", instanceConfig, e);
            e.printStackTrace();
        }
    }
}
