package com.everhomes.rest.contract;

import java.math.BigDecimal;
/**
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
	private String buildings;
	private String apartments;
	private BigDecimal rent;
	private String sponsorName;
	private BigDecimal deposit;
	private String depositStatus;
	private String syncErrorMsg;
	private String status;
	
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	public String getDepositStatus() {
		return depositStatus;
	}
	public void setDepositStatus(String depositStatus) {
		this.depositStatus = depositStatus;
	}
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
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
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
