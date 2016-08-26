package com.everhomes.rest.statistics.transaction;

/**
 *<ul>
 *<li>startDate:开始时间 由選擇日期當天的凌晨開始<li>
 *<li>endDate:结束时间 到選擇日期當天凌晨+1天結束</li>
 *<li>communityId:小區ID</li>
 *<li>namespaceId:域id</li>
 *<li>wareId:商品</li>
 *</ul>
 */
public class ListStatTransactionCommand {
	
	private Long startDate;
    
	private Long endDate;
    
	private Long communityId;
	
	private Integer namespaceId;
	
	private String wareId;

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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	
	
}
