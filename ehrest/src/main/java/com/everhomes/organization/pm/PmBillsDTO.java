package com.everhomes.organization.pm;

import java.math.BigDecimal;

public class PmBillsDTO {
	
	private Long	id;
	
	private String dateStr;
	
	private String address;
	
	private String telephone;
	
	private BigDecimal dueAmount;
	
	private BigDecimal oweAmount;
	
	private BigDecimal totalAmount;
	
	private String     description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public BigDecimal getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}

	public BigDecimal getOweAmount() {
		return oweAmount;
	}

	public void setOweAmount(BigDecimal oweAmount) {
		this.oweAmount = oweAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	

}
