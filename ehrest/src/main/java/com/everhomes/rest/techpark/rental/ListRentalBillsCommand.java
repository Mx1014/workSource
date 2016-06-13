package com.everhomes.rest.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>enterpriseCommunityId：场所id</li>
 * <li>siteType：场所类型</li> 
 * <li>rentalSiteId：场所id</li> 
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
* <li>status：订单状态  0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.rest.techpark.rental.SiteBillStatus}</li>   
 * </ul>
 */
public class ListRentalBillsCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	private String siteType;  
	private Long rentalSiteId ;
	
	private Long startTime; 
	private Long endTime;
	
	private Integer pageOffset;
	private Integer pageSize;
	private Byte billStatus;
	private Byte invoiceFlag;
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

 


	public Integer getPageSize() {
		return pageSize;
	}



	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}



	public Byte getBillStatus() {
		return billStatus;
	}



	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
	}



	public Long getRentalSiteId() {
		return rentalSiteId;
	}



	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}



	public Integer getPageOffset() {
		return pageOffset;
	}



	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}



	public Long getStartTime() {
		return startTime;
	}



	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}



	public Long getEndTime() {
		return endTime;
	}



	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}



	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}



	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
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


 


}
