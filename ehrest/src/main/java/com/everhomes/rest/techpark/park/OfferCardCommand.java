package com.everhomes.rest.techpark.park;

/**
 * 
 * amount: 发卡数
 * 
 * communityId:园区id
 *
 */
public class OfferCardCommand {
	
	private Long communityId;
	
	private Integer amount;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	

}
