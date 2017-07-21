// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>scopeType: 场景类型 PM("pm")：管理公司  ORGANIZATION("organization"): 普通机构  RESIDENTIAL("residential")：小区  COMMERCIAL("commercial")：园区</li>
 * <li>namespaceId: 域空间</li>
 * </ul>
 */
public class ListScopeCommand {

	private String scopeType;

	private Integer namespaceId;

	public ListScopeCommand() {

	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
