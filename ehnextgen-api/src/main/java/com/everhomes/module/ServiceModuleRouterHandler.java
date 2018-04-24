// @formatter:off
package com.everhomes.module;

import com.everhomes.serviceModuleApp.ServiceModuleApp;

/**
 * 推荐在com.everhomes.module.router包内实现接口，方便统一管理
 */
public interface ServiceModuleRouterHandler {
	String SERVICE_MODULE_ROUTER_HANDLER_PREFIX = "ServiceModuleRouterHandler-";

	String getRouter(ServiceModuleApp app);

}