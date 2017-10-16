package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>longitude: 经度</li>
 * <li>latitude: 纬度</li>
 * <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNearbyEnterpriseCustomersCommand {

	private Double longitude;
    
    private Double latitude;
    
    private Byte customerType;
    
    private Long pageAnchor;
	
	private Integer pageSize;
    
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Byte getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Byte customerType) {
		this.customerType = customerType;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
