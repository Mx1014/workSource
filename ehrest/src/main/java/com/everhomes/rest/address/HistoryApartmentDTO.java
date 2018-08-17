// @formatter:off
package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>addressId:房源id</li>
 * <li>apartmentName:房源名称</li>
 * <li>areaSize:建筑面积</li>
 * <li>rentArea:在租面积</li>
 * <li>freeArea:可招租面积</li>
 * <li>chargeArea:收费面积</li>
 * <li>status:房源状态</li>
 * <li>dateBegin: 拆分/合并计划的生效日期</li>
 * <li>remark: 备注</li>
 * </ul>
 */
public class HistoryApartmentDTO {
	
	private Long addressId;
	private String apartmentName;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private Byte status;
	private Long dateBegin;
	private String remark;
	
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public Double getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}
	public Double getRentArea() {
		return rentArea;
	}
	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
	}
	public Double getFreeArea() {
		return freeArea;
	}
	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}
	public Double getChargeArea() {
		return chargeArea;
	}
	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Long dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
