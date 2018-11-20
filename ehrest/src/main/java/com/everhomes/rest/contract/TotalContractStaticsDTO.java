package com.everhomes.rest.contract;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class TotalContractStaticsDTO {
	private Integer communityCount;
	private BigDecimal rentAmount;
	private BigDecimal rentalArea;
	private Integer contractCount;
	private Integer customerCount;
	private Integer orgContractCount;
	private BigDecimal orgContractAmount;
	private Integer userContractCount;
	private BigDecimal userContractAmount;
	private Integer newContractCount;
	private BigDecimal newContractAmount;
	private BigDecimal newContractArea;
	private Integer denunciationContractCount;
	private BigDecimal denunciationContractAmount;
	private BigDecimal denunciationContractArea;
	private Integer changeContractCount;
	private BigDecimal changeContractAmount;
	private BigDecimal changeContractArea;
	private Integer renewContractCount;
	private BigDecimal renewContractAmount;
	private BigDecimal renewContractArea;
	private BigDecimal depositAmount;

	public Integer getCommunityCount() {
		return communityCount;
	}

	public void setCommunityCount(Integer communityCount) {
		this.communityCount = communityCount;
	}

	public BigDecimal getRentAmount() {
		return rentAmount;
	}

	public void setRentAmount(BigDecimal rentAmount) {
		this.rentAmount = rentAmount;
	}

	public BigDecimal getRentalArea() {
		return rentalArea;
	}

	public void setRentalArea(BigDecimal rentalArea) {
		this.rentalArea = rentalArea;
	}

	public Integer getContractCount() {
		return contractCount;
	}

	public void setContractCount(Integer contractCount) {
		this.contractCount = contractCount;
	}

	public Integer getCustomerCount() {
		return customerCount;
	}

	public void setCustomerCount(Integer customerCount) {
		this.customerCount = customerCount;
	}

	public Integer getOrgContractCount() {
		return orgContractCount;
	}

	public void setOrgContractCount(Integer orgContractCount) {
		this.orgContractCount = orgContractCount;
	}

	public BigDecimal getOrgContractAmount() {
		return orgContractAmount;
	}

	public void setOrgContractAmount(BigDecimal orgContractAmount) {
		this.orgContractAmount = orgContractAmount;
	}

	public Integer getUserContractCount() {
		return userContractCount;
	}

	public void setUserContractCount(Integer userContractCount) {
		this.userContractCount = userContractCount;
	}

	public BigDecimal getUserContractAmount() {
		return userContractAmount;
	}

	public void setUserContractAmount(BigDecimal userContractAmount) {
		this.userContractAmount = userContractAmount;
	}

	public Integer getNewContractCount() {
		return newContractCount;
	}

	public void setNewContractCount(Integer newContractCount) {
		this.newContractCount = newContractCount;
	}

	public BigDecimal getNewContractAmount() {
		return newContractAmount;
	}

	public void setNewContractAmount(BigDecimal newContractAmount) {
		this.newContractAmount = newContractAmount;
	}

	public BigDecimal getNewContractArea() {
		return newContractArea;
	}

	public void setNewContractArea(BigDecimal newContractArea) {
		this.newContractArea = newContractArea;
	}

	public Integer getDenunciationContractCount() {
		return denunciationContractCount;
	}

	public void setDenunciationContractCount(Integer denunciationContractCount) {
		this.denunciationContractCount = denunciationContractCount;
	}

	public BigDecimal getDenunciationContractAmount() {
		return denunciationContractAmount;
	}

	public void setDenunciationContractAmount(BigDecimal denunciationContractAmount) {
		this.denunciationContractAmount = denunciationContractAmount;
	}

	public BigDecimal getDenunciationContractArea() {
		return denunciationContractArea;
	}

	public void setDenunciationContractArea(BigDecimal denunciationContractArea) {
		this.denunciationContractArea = denunciationContractArea;
	}

	public Integer getChangeContractCount() {
		return changeContractCount;
	}

	public void setChangeContractCount(Integer changeContractCount) {
		this.changeContractCount = changeContractCount;
	}

	public BigDecimal getChangeContractAmount() {
		return changeContractAmount;
	}

	public void setChangeContractAmount(BigDecimal changeContractAmount) {
		this.changeContractAmount = changeContractAmount;
	}

	public BigDecimal getChangeContractArea() {
		return changeContractArea;
	}

	public void setChangeContractArea(BigDecimal changeContractArea) {
		this.changeContractArea = changeContractArea;
	}

	public Integer getRenewContractCount() {
		return renewContractCount;
	}

	public void setRenewContractCount(Integer renewContractCount) {
		this.renewContractCount = renewContractCount;
	}

	public BigDecimal getRenewContractAmount() {
		return renewContractAmount;
	}

	public void setRenewContractAmount(BigDecimal renewContractAmount) {
		this.renewContractAmount = renewContractAmount;
	}

	public BigDecimal getRenewContractArea() {
		return renewContractArea;
	}

	public void setRenewContractArea(BigDecimal renewContractArea) {
		this.renewContractArea = renewContractArea;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
