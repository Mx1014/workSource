package com.everhomes.rest.xfyun;

import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>routerType: 跳转类型，{@link com.everhomes.rest.xfyun.RouterTypeEnum}</li>
 * <li>appDto: 跳转信息，{@link com.everhomes.rest.launchpadbase.AppDTO}</li>
 * </ul>
 */
public class RouterDTO {
	private Integer routerType;
	private AppDTO appDto;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getRouterType() {
		return routerType;
	}

	public void setRouterType(Integer routerType) {
		this.routerType = routerType;
	}

	public AppDTO getAppDto() {
		return appDto;
	}

	public void setAppDto(AppDTO appDto) {
		this.appDto = appDto;
	}

}
