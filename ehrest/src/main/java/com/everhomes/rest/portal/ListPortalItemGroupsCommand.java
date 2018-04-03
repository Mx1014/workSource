// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>layoutId: 门户layout的id</li>
 * </ul>
 */
public class ListPortalItemGroupsCommand {

	private Long layoutId;

	public ListPortalItemGroupsCommand() {

	}

	public ListPortalItemGroupsCommand(Long layoutId) {
		super();
		this.layoutId = layoutId;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
