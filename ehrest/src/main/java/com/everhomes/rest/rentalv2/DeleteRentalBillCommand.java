package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态 
 * <li>rentalBillId：订单ID</li> 
 * </ul>
 */
public class DeleteRentalBillCommand {

	private Long communityId;
	private String ownerType;
	private Long ownerId;
	@NotNull
	private String siteType; 
	@NotNull
	private Long rentalBillId; 
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 

	 
 
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}



	public Long getRentalBillId() {
		return rentalBillId;
	}



	public void setRentalBillId(Long rentalBillId) {
		this.rentalBillId = rentalBillId;
	}



	public String getOwnerType() {
		return ownerType;
	}



	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}



	public Long getOwnerId() {
		return ownerId;
	}



	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}



	public Long getCommunityId() {
		return communityId;
	}



	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	} 
 
}
