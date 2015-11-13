package com.everhomes.techpark.park;

/**
 * 
 * amount: 发卡数
 * 
 * enterpriseCommunityId:园区id
 *
 */
public class OfferCardCommand {
	
	private Long enterpriseCommunityId;
	
	private Integer amount;

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	

}
