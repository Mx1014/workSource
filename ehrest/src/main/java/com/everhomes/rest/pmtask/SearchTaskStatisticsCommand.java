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
 * <li>currentOrgId: 当前机构id</li>
 * <li>communityId: 所属项目id</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class SearchTaskStatisticsCommand {
	private Integer namespaceId;
	private Long taskCategoryId;
	private String keyword;
	private Long dateStr;
	private Long pageAnchor;
	private Integer pageSize;
	private  Long currentOrgId;
	private  Long communityId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(Long currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

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
