package com.everhomes.xfyun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;
import com.everhomes.util.StringHelper;

@Component
public class XunfeiYunServiceImpl implements XunfeiYunService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XunfeiYunServiceImpl.class);
	
	
	@Override
	public QueryRoutersResponse queryRouters(QueryRoutersCommand cmd) {
		
		LOGGER.info("queryRouters cmd:" + StringHelper.toJsonString(cmd));
		
		XunfeiYunRouterHandler handler = getHandler(cmd);
		if (null != handler) {
			return handler.getRouters(cmd);
		}

		return new QueryRoutersResponse();
	}
	
	
	private XunfeiYunRouterHandler getHandler(QueryRoutersCommand cmd) {
		
		
		
//
//		if (null != moduleId && moduleId > 0) {
//			XunfeiYunRouterHandler handler = null;
//			String handlerPrefix = XunfeiYunRouterHandler.ROUTER_HANDLER_PREFIX;
//			try {
//				handler = PlatformContextNoWarnning.getComponent(handlerPrefix + moduleId);
//			} catch (Exception ex) {
//				 LOGGER.info("XunfeiYunRouterHandler not exist moduleId = {}", moduleId);
//			}
//
//			return handler;
//		}

		return null;
	}
	
	
	
	private Long getHandlerPrefixByQuery(QueryRoutersCommand cmd) {

		if ("物业报修".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.PM_TASK_MODULE;
		}

		if ("服务热线".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.HOTLINE_MODULE;
		}

		if ("访客预约".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.ENTERPRISE_VISITOR_MODULE;
		}
		
		if ("停车缴费".equals(cmd.getRouteTextInfo())) {
			return ServiceModuleConstants.PARKING_MODULE;
		}

		if ("我的工单".equals(cmd.getRouteTextInfo())) { // 我的申请页面
			return null;
		}
		
		if ("悦邻优选".equals(cmd.getRouteTextInfo())) {
			return null;
		}

		return null;
	}
}
