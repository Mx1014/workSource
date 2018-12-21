package com.everhomes.portal;

import com.everhomes.enterprisemoment.EnterpriseMomentConstants;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.portal.HandlerGetAppInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerGetItemActionDataCommand;
import com.everhomes.rest.portal.HandlerProcessInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerPublishCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.user.UserContext;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.ENTERPRISE_MOMENT_MODULE)
public class EnterpriseMomentPublishHandler implements PortalPublishHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseMomentPublishHandler.class);

    @Autowired
    private LocaleStringService localeStringService;

	@Override
	public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
		return getInstanceConfig(namespaceId,cmd.getAppId());
	} 

	private String getInstanceConfig(Integer namespaceId, Long appId) {
		StringBuffer sb = new StringBuffer();
		String localeString = "zh_CN";
		if(UserContext.current() != null && UserContext.current().getUser() !=null)
			localeString = UserContext.current().getUser().getLocale();
		//displayName={displayName}&appId={appId}&organizationId={organizationId}

		sb.append("displayName=");
		sb.append(localeStringService.getLocalizedString(EnterpriseMomentConstants.SELECTOR_TYPE_SCOP, EnterpriseMomentConstants.TITLE, localeString, "同事圈"));
		return sb.toString();
	}

	@Override
	public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
		return  getInstanceConfig(namespaceId,cmd.getAppId());
	}

	@Override
	public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
		return  getInstanceConfig(namespaceId,cmd.getAppId());
	}

	@Override
	public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
		return actionData;
	}
	

}
