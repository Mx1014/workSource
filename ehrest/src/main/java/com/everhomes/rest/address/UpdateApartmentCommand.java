// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: 门牌id</li>
 * <li>apartmentName: 门牌名称</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.organization.pm.AddressMappingStatus}</li>
 * <li>areaSize: 面积</li>
 * </ul>
 */
public class UpdateApartmentCommand {
	private Long id;
	private String apartmentName;
	private Byte status;
	private Double areaSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
