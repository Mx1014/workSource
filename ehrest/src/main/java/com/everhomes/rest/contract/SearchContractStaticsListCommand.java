package com.everhomes.rest.contract;

import java.util.List;

import com.everhomes.util.StringHelper;

public class SearchContractStaticsListCommand {

	private Integer namespaceId;
	private Long orgId;
	private List<Long> communityIds;
	private Byte dateType;
	private Long pageAnchor;
	private Integer pageSize;
	private Long pageNumber;
	private String startTimeStr;
	private String endTimeStr;
	private String dateStr;

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public List<Long> getCommunityIds() {
		return communityIds;
	}

	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Byte getDateType() {
		return dateType;
	}

	public void setDateType(Byte dateType) {
		this.dateType = dateType;
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

	public Long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
