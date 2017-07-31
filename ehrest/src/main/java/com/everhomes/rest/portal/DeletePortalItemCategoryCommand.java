// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: item分类的id</li>
 * <li>moveItemCategoryId: 移动到某一个分类组的id</li>
 * </ul>
 */
public class DeletePortalItemCategoryCommand {

	private Long id;

	private Long moveItemCategoryId;

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

	public Long getMoveItemCategoryId() {
		return moveItemCategoryId;
	}

	public void setMoveItemCategoryId(Long moveItemCategoryId) {
		this.moveItemCategoryId = moveItemCategoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
