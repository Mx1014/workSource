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
 * Created by ying.xiong on 2018/1/22.
 */
public class ContractExportDetailDTO {
	private String contractNumber;
	private String name;
	private Timestamp contractStartDate;
	private Timestamp contractEndDate;
	private Byte contractType;
	private String customerName;
	private String buildings;
	private BigDecimal rent;
	private Byte status;
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
	public Timestamp getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public Timestamp getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public Byte getContractType() {
		return contractType;
	}
	public void setContractType(Byte contractType) {
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
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
}
