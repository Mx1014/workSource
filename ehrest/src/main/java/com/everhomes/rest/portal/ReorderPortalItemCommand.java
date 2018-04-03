// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>reorders: item排序集合</li>
 * </ul>
 */
public class ReorderPortalItemCommand {

	@ItemType(PortalItemReorder.class)
	private List<PortalItemReorder> reorders;

	public ReorderPortalItemCommand() {

	}

	public ReorderPortalItemCommand(List<PortalItemReorder> reorders) {
		super();
		this.reorders = reorders;
	}

	public List<PortalItemReorder> getReorders() {
		return reorders;
	}

	public void setReorders(List<PortalItemReorder> reorders) {
		this.reorders = reorders;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
