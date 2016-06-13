package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>enterpriseCommunityId：场所id</li>
 * <li>siteType：场所类型</li> 
 * <li>rentalSiteId：场所ID</li> 
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
* <li>status：订单状态  0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.rest.techpark.rental.SiteBillStatus}</li>   
 * </ul>
 */
public class CompleteBillCommand {


	private String ownerType;
	private Long ownerId;
	private String siteType;  
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




	public Long getRentalBillId() {
		return rentalBillId;
	}




	public void setRentalBillId(Long rentalBillId) {
		this.rentalBillId = rentalBillId;
	}


 

 



}
