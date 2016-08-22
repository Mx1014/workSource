package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>rentalSiteId：资源id</li>
 * <li>organizationId：机构id</li> 
 * <li>resourceTypeId：图标id</li> 
 * <li>startTime：开始时间</li> 
 * <li>endTime：结束时间</li> 
 * <li>vendorType：支付方式,10001-支付宝，10002-微信</li> 
* <li>billStatus：订单状态  0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.rest.rentalv2.SiteBillStatus}</li>   
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListRentalBillsCommand { 
	@NotNull
	private Long organizationId; 
	private Long rentalSiteId ;
	private java.lang.Long       resourceTypeId; 
	
	private Long startTime; 
	private Long endTime;
	private java.lang.String     vendorType;
	private Byte billStatus;  
	private Long pageAnchor;
    
	private Integer pageSize;
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}

	public java.lang.Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(java.lang.Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
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

	public java.lang.String getVendorType() {
		return vendorType;
	}

	public void setVendorType(java.lang.String vendorType) {
		this.vendorType = vendorType;
	}

	public Byte getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
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
 
	  
 


}
