package com.everhomes.rest.techpark.park;

import java.sql.Timestamp;

public class ApplyParkCardDTO {
	
	private Long id;
	
	private String applierName;
	
	private String applierPhone;
	
	private String companyName;
	
	private String plateNumber;
	
	private Timestamp applyTime;
	
	private Byte applyStatus;
	
	private Byte fetchStatus;

	private Timestamp deadline;
	public Timestamp getDeadline() {
		return deadline;
	}

	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getApplierPhone() {
		return applierPhone;
	}

	public void setApplierPhone(String applierPhone) {
		this.applierPhone = applierPhone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public Timestamp getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public Byte getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Byte applyStatus) {
		this.applyStatus = applyStatus;
	}

	public Byte getFetchStatus() {
		return fetchStatus;
	}

	public void setFetchStatus(Byte fetchStatus) {
		this.fetchStatus = fetchStatus;
	}

}
