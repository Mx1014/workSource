// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>reorders: itemGroup集合</li>
 * </ul>
 */
public class ReorderPortalItemGroupCommand {

	@ItemType(PortalItemGroupReorder.class)
	private List<PortalItemGroupReorder> reorders;

	public ReorderPortalItemGroupCommand() {

	}

	public ReorderPortalItemGroupCommand(List<PortalItemGroupReorder> reorders) {
		super();
		this.reorders = reorders;
	}

	public List<PortalItemGroupReorder> getReorders() {
		return reorders;
	}

	public void setReorders(List<PortalItemGroupReorder> reorders) {
		this.reorders = reorders;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
