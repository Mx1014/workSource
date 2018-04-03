// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>scopeType: 场景类型 PM("pm")：管理公司  ORGANIZATION("organization"): 普通机构  RESIDENTIAL("residential")：小区  COMMERCIAL("commercial")：园区</li>
 * <li>namespaceId: 域空间</li>
 * <li>keywords: 关键字</li>
 * <li>pageSize: 每页记录数</li>
 * <li>anchor: 下一页锚点</li>
 * </ul>
 */
public class ListScopeCommand {

	private Integer pageSize;

	private Long anchor;

	private String scopeType;

	private Integer namespaceId;

	private String keywords;

	public ListScopeCommand() {

	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getAnchor() {
		return anchor;
	}

	public void setAnchor(Long anchor) {
		this.anchor = anchor;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
