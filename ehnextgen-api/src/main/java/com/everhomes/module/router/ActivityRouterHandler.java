// @formatter:off
package com.everhomes.module.router;

import com.everhomes.module.ServiceModuleRouterHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import org.springframework.stereotype.Component;


@Component(ServiceModuleRouterHandler.SERVICE_MODULE_ROUTER_HANDLER_PREFIX + ServiceModuleConstants.ACTIVITY_MODULE)
public class ActivityRouterHandler implements ServiceModuleRouterHandler {


	@Override
	public String getRouter(ServiceModuleApp app) {

		return "zl://activity?a=a&b=b&c=c";
	}
}