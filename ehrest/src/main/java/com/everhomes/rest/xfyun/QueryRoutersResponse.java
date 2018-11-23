package com.everhomes.rest.xfyun;

import java.util.List;

import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>routerDtos: 跳转路径，{@link com.everhomes.rest.launchpadbase.AppDTO}</li>
 * </ul>
 */
public class QueryRoutersResponse {
	
	private List<AppDTO> routerDtos;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<AppDTO> getRouterDtos() {
		return routerDtos;
	}

	public void setRouterDtos(List<AppDTO> routerDtos) {
		this.routerDtos = routerDtos;
	}


}
