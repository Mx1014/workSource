// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: item分类的id</li>
 * </ul>
 */
public class DeletePortalItemCategoryCommand {

	private Long id;

	public DeletePortalItemCategoryCommand() {

	}

	public DeletePortalItemCategoryCommand(Long id) {
		super();
		this.id = id;
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
