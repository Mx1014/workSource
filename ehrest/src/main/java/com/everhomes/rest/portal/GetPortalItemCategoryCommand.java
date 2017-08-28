// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 分类id</li>
 * </ul>
 */
public class GetPortalItemCategoryCommand {

	private Long id;

	public GetPortalItemCategoryCommand() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
