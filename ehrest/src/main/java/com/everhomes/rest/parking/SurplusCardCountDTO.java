package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

public class SurplusCardCountDTO {
	
	private Integer totalCount;
	private Integer surplusCount;
	private Byte maxIssueNumFlag;
	
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getSurplusCount() {
		return surplusCount;
	}
	public void setSurplusCount(Integer surplusCount) {
		this.surplusCount = surplusCount;
	}
	
	public Byte getMaxIssueNumFlag() {
		return maxIssueNumFlag;
	}
	public void setMaxIssueNumFlag(Byte maxIssueNumFlag) {
		this.maxIssueNumFlag = maxIssueNumFlag;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
