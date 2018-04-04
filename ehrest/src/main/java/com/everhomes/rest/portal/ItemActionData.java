// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>layoutId: 门户id</li>
 *     <li>moduleAppId: 应用id</li>
 * </ul>
 */
public class ItemActionData {

	private Long layoutId;
	private Long moduleAppId;

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	public Long getModuleAppId() {
		return moduleAppId;
	}

	public void setModuleAppId(Long moduleAppId) {
		this.moduleAppId = moduleAppId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
