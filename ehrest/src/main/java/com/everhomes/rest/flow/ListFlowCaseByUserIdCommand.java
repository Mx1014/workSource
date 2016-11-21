package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class ListFlowCaseByUserIdCommand {
    private Integer pageSize;
    private Long anchor;
	private Long userId;
	private Byte flowCaseStatus;

	public Byte getFlowCaseStatus() {
		return flowCaseStatus;
	}

	public void setFlowCaseStatus(Byte flowCaseStatus) {
		this.flowCaseStatus = flowCaseStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getAnchor() {
		return anchor;
	}

	public void setAnchor(Long anchor) {
		this.anchor = anchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
