package com.everhomes.approval;

import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhApprovalRequests;

public class ApprovalRequest extends EhApprovalRequests {

	private static final long serialVersionUID = -8907821627038769313L;

	private List<ApprovalDayActualTime> approvalDayActualTimeList;

	public List<ApprovalDayActualTime> getApprovalDayActualTimeList() {
		return approvalDayActualTimeList;
	}

	public void setApprovalDayActualTimeList(List<ApprovalDayActualTime> approvalDayActualTimeList) {
		this.approvalDayActualTimeList = approvalDayActualTimeList;
	}
	
}