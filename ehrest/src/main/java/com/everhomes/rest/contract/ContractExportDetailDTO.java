package com.everhomes.rest.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *  <li>communtiyName:楼栋名</li>
 *  <li>name:楼栋名</li>
 *  <li>aliasName:楼栋别名</li>
 *  <li>managerName:联系人</li>
 *  <li>contact:联系电话</li>
 *  <li>address:地址</li>
 *  <li>areaSize:占地面积</li>
 *  <li>latitudeLongitude:经纬度</li>
 *  <li>buildingNumber:楼栋编号</li>
 *  <li>trafficDescription:交通说明</li>
 *  <li>trafficDescription:楼栋介绍</li>
 * </ul>
 * Created by jm.ding on 2018/7/9.
 */
public class ContractExportDetailDTO {
	private String contractNumber;
	private String name;
	private String contractStartDate;
	private String contractEndDate;
	private String contractType;
	private String customerName;
	private String customerId;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getApartments() {
		return apartments;
	}
	public void setApartments(String apartments) {
		this.apartments = apartments;
	}
	private String buildings;
	private String apartments;
	private BigDecimal rent;
	private String syncErrorMsg;
	private String status;
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public String getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getBuildings() {
		return buildings;
	}
	public void setBuildings(String buildings) {
		this.buildings = buildings;
	}
	public BigDecimal getRent() {
		return rent;
	}
	public void setRent(BigDecimal rent) {
		this.rent = rent;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getSyncErrorMsg() {
		return syncErrorMsg;
	}

	public void setSyncErrorMsg(String syncErrorMsg) {
		this.syncErrorMsg = syncErrorMsg;
	}
}
