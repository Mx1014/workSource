package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
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
public class BatchIncompleteBillCommand {


	private String ownerType;
	private Long ownerId;
	private String siteType;  
	@ItemType(Long.class)
	private List<Long> rentalBillIds;
	
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


 



	public List<Long> getRentalBillIds() {
		return rentalBillIds;
	}




	public void setRentalBillIds(List<Long> rentalBillIds) {
		this.rentalBillIds = rentalBillIds;
	}


 

 



}
