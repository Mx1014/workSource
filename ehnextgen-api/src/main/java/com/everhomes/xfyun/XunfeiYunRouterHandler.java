package com.everhomes.xfyun;

import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;

public interface XunfeiYunRouterHandler {

	public static final String ROUTER_HANDLER_PREFIX = "XUNFEI_YUN_ROUTER_HANDLER_PREFIX";

	public QueryRoutersResponse getRouters(QueryRoutersCommand cmd);

}
