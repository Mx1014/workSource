package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>查询订单
 * <li>rentalSiteId：资源id</li>
 * <li>organizationId：机构id</li>
 * <li>resourceType：资源类型 {@link com.everhomes.rest.rentalv2.RentalResourceType}</li>
 * <li>resourceTypeId：资源类型id</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListRentalOrdersCommand {
	@NotNull
	private Long organizationId;
	private Long rentalSiteId ;
	private String resourceType;
	private Long resourceTypeId;

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

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
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
