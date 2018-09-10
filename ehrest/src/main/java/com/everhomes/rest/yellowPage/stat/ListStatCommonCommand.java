package com.everhomes.rest.yellowPage.stat;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type:服务联盟类型</li>
 * <li>ownerId:所属项目id</li>
 * <li>categoryId: 服务类型id</li>
 * <li>searchType: 0-根据服务类型做统计 1-统计服务</li>
 * <li>startDate: 开始日期，默认一个月前</li>
 * <li>endDate: 结束日期，默认当天的前一天</li>
 * <li>sortOrder: 0-降序 1-升序</li>
 * <li>pageAnchor: 下一页锚点（新版本使用）</li>
 * <li>pageSize: 每页的数量</li>
 * <li>currentPMId: 管理公司id</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 应用的originId</li>
 * </ul>
 */
public class ListStatCommonCommand {

	@NotNull
	private Long type;
	private Long ownerId;
	private Long categoryId;
	private Byte searchType;
	private String startDate;
	private String endDate;
	private Byte sortOrder;
	private Long pageAnchor;
	private Integer pageSize;

	@NotNull
	private Long currentPMId;
	@NotNull
	private Long currentProjectId;
	@NotNull
	private Long appId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getSearchType() {
		return searchType;
	}

	public void setSearchType(Byte searchType) {
		this.searchType = searchType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public Byte getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Byte sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

}

