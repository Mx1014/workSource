// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>itemGroupId: 门户itemGroup的id</li>
 * <li>scopeType: 场景类型</li>
 * </ul>
 */
public class ListPortalItemsCommand {

	private Long pageAnchor;

	private Integer pageSize;

	private Long itemGroupId;

	private String scopeType;

	public ListPortalItemsCommand() {

	}

	public ListPortalItemsCommand(Long itemGroupId, String scopeType) {
		super();
		this.itemGroupId = itemGroupId;
		this.scopeType = scopeType;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getItemGroupId() {
		return itemGroupId;
	}

	public void setItemGroupId(Long itemGroupId) {
		this.itemGroupId = itemGroupId;
	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
