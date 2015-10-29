package com.everhomes.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态 
 * <li>rentalBillId：订单ID</li> 
 * </ul>
 */
public class DeleteRentalBillCommand {
	@NotNull
	private Long enterpriseCommunityId;
	@NotNull
	private String siteType; 
	@NotNull
	private Long rentalBillId; 
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 

	 

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
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
 
}
