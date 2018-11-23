package com.everhomes.rest.contract.statistic;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 * 	<li>namespaceId</li>
 *	<li>communityIds : communityId的数组</li>
 *	<li>dateStr : 时间（传年份例子：2018，传月份例子：2018-07）</li>
 *	<li>dateType : 日期查询类型 参考{@link com.everhomes.rest.contract.statistic.ContractStatisticDateType}</li>
 *	<li>startTimeStr : 开始时间</li>
 *	<li>endTimeStr : 结束时间</li>
 *	<li>searchType : 查询类型 参考{@link com.everhomes.rest.contract.statistic.ContractStatisticSearchType}</li>
 *</ul>
 */

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
	private Byte searchType;

	public Byte getSearchType() {
		return searchType;
	}

	public void setSearchType(Byte searchType) {
		this.searchType = searchType;
	}

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
