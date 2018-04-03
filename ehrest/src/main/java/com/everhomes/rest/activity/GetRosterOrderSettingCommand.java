// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>categoryId: categoryId</li>
 * </ul>
 */
public class GetRosterOrderSettingCommand {
	private Integer namespaceId;
	private Long categoryId;

	public GetRosterOrderSettingCommand() {
		super();
	}

	public GetRosterOrderSettingCommand(Integer namespaceId, Long categoryId) {
		super();
		this.namespaceId = namespaceId;
		this.categoryId = categoryId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
