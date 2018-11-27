package com.everhomes.rest.community;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *     <li>buildingId: 楼栋id</li>
 *     <li>buildingName: 楼栋名称</li>
 *     <li>address: 地址</li>
 *     <li>apartmentNumber: 园区下的门牌数量</li>
 *     <li>areaSize: 建筑面积</li>
 *     <li>rentArea: 在租面积</li>
 *     <li>freeArea: 可招租面积</li>
 *     <li>chargeArea: 收费面积</li>
 *     <li>relatedContractNumber: 在租合同数</li>
 *     <li>areaAveragePrice: 在租实时均价（元/平方米）</li>
 *     <li>relatedEnterpriseCustomerNumber: 入驻企业数</li>
 *     <li>relatedOrganizationOwnerNumber: 入驻用户数</li>
 *     <li>totalRent: 在租合同总金额</li>
 *     <li>freeRate: 空置率，空置率=总的可招租面积/总的建筑面积*100%</li>
 * </ul>
 */
public class BuildingStatisticsDTO {
	
	private Long buildingId;
	private String buildingName;
	private String address;
	private Integer apartmentNumber;
	private Double areaSize;
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private Integer relatedContractNumber;
	private Double areaAveragePrice;
	private Integer relatedEnterpriseCustomerNumber;
	private Integer relatedOrganizationOwnerNumber;
	private Double totalRent;
	private BigDecimal freeRate;
	
	public Double getTotalRent() {
		return totalRent;
	}
	public void setTotalRent(Double totalRent) {
		this.totalRent = totalRent;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getApartmentNumber() {
		return apartmentNumber;
	}
	public void setApartmentNumber(Integer apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
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
	public Integer getRelatedContractNumber() {
		return relatedContractNumber;
	}
	public void setRelatedContractNumber(Integer relatedContractNumber) {
		this.relatedContractNumber = relatedContractNumber;
	}
	public Double getAreaAveragePrice() {
		return areaAveragePrice;
	}
	public void setAreaAveragePrice(Double areaAveragePrice) {
		this.areaAveragePrice = areaAveragePrice;
	}
	public Integer getRelatedEnterpriseCustomerNumber() {
		return relatedEnterpriseCustomerNumber;
	}
	public void setRelatedEnterpriseCustomerNumber(Integer relatedEnterpriseCustomerNumber) {
		this.relatedEnterpriseCustomerNumber = relatedEnterpriseCustomerNumber;
	}
	public Integer getRelatedOrganizationOwnerNumber() {
		return relatedOrganizationOwnerNumber;
	}
	public void setRelatedOrganizationOwnerNumber(Integer relatedOrganizationOwnerNumber) {
		this.relatedOrganizationOwnerNumber = relatedOrganizationOwnerNumber;
	}
	public BigDecimal getFreeRate() {
		return freeRate;
	}
	public void setFreeRate(BigDecimal freeRate) {
		this.freeRate = freeRate;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
