package com.everhomes.portal;

import com.everhomes.rest.portal.HandlerGetAppInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerGetItemActionDataCommand;
import com.everhomes.rest.portal.HandlerProcessInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerPublishCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.common.ServiceModuleConstants;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.ENTERPRISE_PAYMENT_AUTH)
public class EnterprisePaymentAuthPortalPublishHandler implements PortalPublishHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterprisePaymentAuthPortalPublishHandler.class);
	
	@Override
	public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
		return getNewInstanceConfig(instanceConfig, namespaceId);
	} 
	
	private String getNewInstanceConfig(String instanceConfig, Integer namespaceId) {
		return "{\"url\":\"${home.url}/enterprise-payment-auth/build/index.html?namespaceId=" + namespaceId + "#/home#sign_suffix\"}";
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
