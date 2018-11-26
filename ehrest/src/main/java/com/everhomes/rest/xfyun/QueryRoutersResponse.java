package com.everhomes.rest.xfyun;

import java.util.List;

import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>routerDtos: 跳转路径，{@link com.everhomes.rest.xfyun.RouterDTO}</li>
 * </ul>
 */
public class QueryRoutersResponse {

	private List<RouterDTO> routerDtos;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<RouterDTO> getRouterDtos() {
		return routerDtos;
	}

	public void setRouterDtos(List<RouterDTO> routerDtos) {
		this.routerDtos = routerDtos;
	}

}
