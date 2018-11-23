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
public class GetTotalContractStaticsCommand {
	
	private Integer namespaceId;
	private List<Long> communityIds;
	private String dateStr;
	private Byte dateType;
	private String startTimeStr;
	private String endTimeStr;
	private Long pageAnchor;
    private Integer pageSize;
    private String searchType;
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
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
	public Byte getDateType() {
		return dateType;
	}
	public void setDateType(Byte dateType) {
		this.dateType = dateType;
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
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public List<Long> getCommunityIds() {
		return communityIds;
	}
	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
