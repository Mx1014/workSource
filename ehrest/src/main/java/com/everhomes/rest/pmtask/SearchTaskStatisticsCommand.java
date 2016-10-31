package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>taskCategoryId: 服务类型</li>
 * <li>keyword: 关键字</li>
 * <li>dateStr: 日期</li>
 * <li>pageAnchor: 分页瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class SearchTaskStatisticsCommand {
	private Integer namespaceId;
	private Long taskCategoryId;
	private String keyword;
	private Long dateStr;
	private Long pageAnchor;
	private Integer pageSize;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getTaskCategoryId() {
		return taskCategoryId;
	}
	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
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
