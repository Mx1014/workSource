package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

public class EvaluateTaskCommand {
	private String ownerType;
    private Long ownerId;
    private Long id;
    private Integer evaluateScore;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getEvaluateScore() {
		return evaluateScore;
	}
	public void setEvaluateScore(Integer evaluateScore) {
		this.evaluateScore = evaluateScore;
	}
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
