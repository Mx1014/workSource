// @formatter:off
package com.everhomes.rest.locale;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>scope: 范围</li>
 * <li>code: 代码</li>
 * <li>namespaceId: 域空间id</li>
 * <li>keyword: 查询关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListLocaleTemplateCommand {
	private String scope;
	private Integer code;
	private Integer namespaceId;
	private String keyword;
	private Long pageAnchor;
	private Integer pageSize;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
