// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>reorders: 分类的排序集合</li>
 * </ul>
 */
public class ReorderPortalItemCategoryCommand {

	@ItemType(PortalItemCategoryReorder.class)
	private List<PortalItemCategoryReorder> reorders;

	public ReorderPortalItemCategoryCommand() {

	}

	public ReorderPortalItemCategoryCommand(List<PortalItemCategoryReorder> reorders) {
		super();
		this.reorders = reorders;
	}

	public List<PortalItemCategoryReorder> getReorders() {
		return reorders;
	}

	public void setReorders(List<PortalItemCategoryReorder> reorders) {
		this.reorders = reorders;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
