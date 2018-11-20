package com.everhomes.xfyun.handler;

import org.springframework.stereotype.Component;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;
import com.everhomes.xfyun.XunfeiYunRouterHandler;

@Component(XunfeiYunRouterHandler.ROUTER_HANDLER_PREFIX + ServiceModuleConstants.PM_TASK_MODULE)
public class XunfeiPmTaskHandler implements XunfeiYunRouterHandler{

	@Override
	public QueryRoutersResponse getRouters(QueryRoutersCommand cmd) {
		
		return null;
	}

}
