// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appId: 应用表主键Id</li>
 *     <li>appOriginId: 应用跨版本不变id</li>
 * </ul>
 */
public class HandlerPublishCommand {


	private Long appId;

	private Long appOriginId;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getAppOriginId() {
		return appOriginId;
	}

	public void setAppOriginId(Long appOriginId) {
		this.appOriginId = appOriginId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
