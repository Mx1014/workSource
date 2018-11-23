package com.everhomes.xfyun;

import java.util.List;

import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

public interface XunfeiYunRouterHandler {

	public static final String ROUTER_HANDLER_PREFIX = "XUNFEI_YUN_ROUTER_HANDLER_PREFIX";

	public QueryRoutersResponse getRouters(QueryRoutersCommand cmd, List<ServiceModuleApp> apps);

}
