package com.everhomes.rest.techpark.park;

public class SetPreferentialRuleCommand {
	
	private Long startTime;
	
	private Long endTime;
	
	private Long beforeNember;
	
	private Long ownerId;
	
	private String ownerType;

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getBeforeNember() {
		return beforeNember;
	}

	public void setBeforeNember(Long beforeNember) {
		this.beforeNember = beforeNember;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	
	

}
