// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * <li>itemGroupId: item group id</li>
 * </ul>
 */
public class ListPortalItemCategoriesCommand {

	private Integer namespaceId;

	private Long itemGroupId;

	public ListPortalItemCategoriesCommand() {

	}

	public ListPortalItemCategoriesCommand(Integer namespaceId) {
		super();
		this.namespaceId = namespaceId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getItemGroupId() {
		return itemGroupId;
	}

	public void setItemGroupId(Long itemGroupId) {
		this.itemGroupId = itemGroupId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
