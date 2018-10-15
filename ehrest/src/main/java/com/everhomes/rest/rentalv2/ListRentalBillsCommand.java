package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单
 * <li>rentalSiteId：资源id</li>
 * <li>organizationId：机构id</li> 
 * <li>resourceTypeId：图标id</li>
 * <li>resourceType：资源类型 {@link com.everhomes.rest.rentalv2.RentalV2ResourceType}</li>
 * <li>startTime：开始时间</li> 
 * <li>endTime：结束时间</li> 
 * <li>vendorType：支付方式,10001-支付宝，10002-微信</li> 
* <li>billStatus：订单状态  0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.rest.rentalv2.SiteBillStatus}</li>
 * <li>payChannel：支付类型  0个人支付1企业支付(记账)2企业支付(完成)  参考{@link com.everhomes.rest.rentalv2.PayChannel}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class ListRentalBillsCommand {
	private Long organizationId;
	private Long communityId;
	private Long rentalSiteId ;
	private java.lang.Long       resourceTypeId;
	private String resourceType;
	
	private Long startTime; 
	private Long endTime;
	private java.lang.String     vendorType;
	private Byte billStatus;
	private String payChannel;
	private Long pageAnchor;
    
	private Integer pageSize;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
}
