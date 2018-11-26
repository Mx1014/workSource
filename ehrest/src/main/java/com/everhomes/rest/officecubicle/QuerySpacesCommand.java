package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
 * <li>ownerType : community </li>
 * <li>ownerId : communityId 园区id</li>
 * <li>provinceName: 省名称</li>
 * <li>cityName: 城市名</li>
 * <li>pageAnchor: 锚点</li>
 * <li>rentType:</li>
 * <li>pageSize: 一页的大小</li> 
 * </ul>
 */
public class QuerySpacesCommand { 
	
	private String ownerType;
	private Long ownerId;
	private String provinceName;
	private String cityName;
	private Byte rentType;
	private Long pageAnchor;
    
	private Integer pageSize;
 
	public Byte getRentType() {
		return rentType;
	}


	public void setRentType(Byte rentType) {
		this.rentType = rentType;
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
