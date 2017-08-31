// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>resourceTypeId: 资源id</li>
 * <li>pageType: 样式</li>
 * </ul>
 */
public class RentalInstanceConfig {

	private Long resourceTypeId;

	private Byte pageType;

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Byte getPageType() {
		return pageType;
	}

	public void setPageType(Byte pageType) {
		this.pageType = pageType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
