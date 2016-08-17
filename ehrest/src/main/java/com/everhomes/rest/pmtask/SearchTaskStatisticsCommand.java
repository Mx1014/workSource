package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>categoryId: 服务类型</li>
 * <li>keyword: 关键字</li>
 * <li>dateStr: 日期</li>
 * </ul>
 */
public class SearchTaskStatisticsCommand {
	private Integer namespaceId;
	private Long categoryId;
	private String keyword;
	private Long dateStr;
	
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
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Long getDateStr() {
		return dateStr;
	}
	public void setDateStr(Long dateStr) {
		this.dateStr = dateStr;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
