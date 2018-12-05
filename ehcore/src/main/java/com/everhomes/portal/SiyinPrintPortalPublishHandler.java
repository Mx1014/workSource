package com.everhomes.portal;

import com.everhomes.rest.portal.HandlerGetAppInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerGetItemActionDataCommand;
import com.everhomes.rest.portal.HandlerProcessInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerPublishCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetModuleAppMapping;
import com.everhomes.asset.AssetService;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.print.SiyinPrintInstanceConfig;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.PRINT_MODULE)
public class SiyinPrintPortalPublishHandler implements PortalPublishHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintPortalPublishHandler.class);
	
    @Autowired
    protected AssetService assetService;

	@Override
	public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
		return getNewInstanceConfig(instanceConfig, namespaceId);
	} 
	
	private String getNewInstanceConfig(String instanceConfig, Integer namespaceId) {
		int index = instanceConfig.indexOf("namespaceId=");
		if (index < 0) {
			instanceConfig = instanceConfig.replaceAll("#/home#sign_suffix", "?namespaceId="+namespaceId+"#/home#sign_suffix");
		}
		
		return instanceConfig;
	}

	@Override
	public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
		return getNewInstanceConfig(instanceConfig, namespaceId);
	}

	@Override
	public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
		return getNewInstanceConfig(instanceConfig, namespaceId);
	}

	@Override
	public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
		return actionData;
	}
	

}
