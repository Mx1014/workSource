// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * </ul>
 */
public class ListPortalItemCategoriesCommand {

	private Long namespaceId;

	public ListPortalItemCategoriesCommand() {

	}

	public ListPortalItemCategoriesCommand(Long namespaceId) {
		super();
		this.namespaceId = namespaceId;
	}

	public Long getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Long namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
