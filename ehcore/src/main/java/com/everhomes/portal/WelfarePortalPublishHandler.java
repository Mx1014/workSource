package com.everhomes.portal;

import com.everhomes.rest.portal.HandlerGetAppInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerGetItemActionDataCommand;
import com.everhomes.rest.portal.HandlerProcessInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerPublishCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.common.ServiceModuleConstants;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.WELFARE_MODULE)
public class WelfarePortalPublishHandler implements PortalPublishHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(WelfarePortalPublishHandler.class);
	

	@Override
	public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
		return getInstanceConfig(namespaceId);
	} 

	private String getInstanceConfig(Integer namespaceId) {
		return "{\"url\":\"${home.url}/enterprise-welfare/build/index.html?ns=" + namespaceId + "#/#sign_suffix\"}";
	}

	@Override
	public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
		return getInstanceConfig(namespaceId);
	}

	@Override
	public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
		return getInstanceConfig(namespaceId);
	}

	@Override
	public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
		return actionData;
	}
	

}
