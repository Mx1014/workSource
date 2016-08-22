package com.everhomes.rest.journal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>keyword: 关键字</li>
 * <li>pageAnchor: 分页瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class ListJournalsCommand {
	private Integer namespaceId;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;
    
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
