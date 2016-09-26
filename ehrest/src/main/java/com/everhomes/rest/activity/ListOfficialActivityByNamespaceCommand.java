// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>tag:活动标签</li>
 * <li>scope: 范围，{@link com.everhomes.rest.ui.user.ActivityLocationScope}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 *</ul>
 */
public class ListOfficialActivityByNamespaceCommand {
	private Integer namespaceId;
	private String tag;
	private Byte scope;
	private Long pageAnchor;
	private Integer pageSize;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Byte getScope() {
		return scope;
	}

	public void setScope(Byte scope) {
		this.scope = scope;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
