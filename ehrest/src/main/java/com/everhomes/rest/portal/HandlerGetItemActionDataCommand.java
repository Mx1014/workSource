// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appId: 应用表主键Id</li>
 *     <li>appOriginId: 应用跨版本不变id</li>
 *     <li>moduleEntryId: 应用入口ID</li>
 * </ul>
 */
public class HandlerGetItemActionDataCommand {

	private Long appId;

	private Long appOriginId;

	private Long moduleEntryId;

	public Long getModuleEntryId() {
		return moduleEntryId;
	}

	public void setModuleEntryId(Long moduleEntryId) {
		this.moduleEntryId = moduleEntryId;
	}

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
