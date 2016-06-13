package com.everhomes.rest.techpark.park;

public class PreferentialRulesDTO {
	
	private Long id;
	
	private String   ownerType;
	private Long     ownerId;
	private Long startTime;
	private Long endTime;
	private String   type;
	private Long     beforeNember;
	private String   paramsJson;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getBeforeNember() {
		return beforeNember;
	}
	public void setBeforeNember(Long beforeNember) {
		this.beforeNember = beforeNember;
	}
	public String getParamsJson() {
		return paramsJson;
	}
	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	
	
}
