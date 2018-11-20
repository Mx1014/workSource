package com.everhomes.rest.xfyun;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>routers: 跳转路径，{@link com.everhomes.rest.xfyun.XfRouterDTO}</li>
 * </ul>
 */
public class QueryRoutersResponse {
	
	private List<XfRouterDTO> routers;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<XfRouterDTO> getRouters() {
		return routers;
	}

	public void setRouters(List<XfRouterDTO> routers) {
		this.routers = routers;
	}

}
