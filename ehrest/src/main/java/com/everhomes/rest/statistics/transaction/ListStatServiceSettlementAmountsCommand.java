package com.everhomes.rest.statistics.transaction;

/**
 *<ul>
 *<li>startDate:开始时间 由選擇日期當天的凌晨開始<li>
 *<li>endDate:结束时间 到選擇日期當天凌晨+1天結束</li>
 *<li>communityId:小區ID</li>
 *<li>namespaceId:域id</li>
 *</ul>
 */
public class ListStatServiceSettlementAmountsCommand {
	
	private Long startDate;
    
	private Long endDate;
    
	private Long communityId;
	
	private Long namespaceId;

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Long namespaceId) {
		this.namespaceId = namespaceId;
	}
	
}
