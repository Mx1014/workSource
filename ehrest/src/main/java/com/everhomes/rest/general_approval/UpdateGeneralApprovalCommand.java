package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

public class UpdateGeneralApprovalCommand {
	private Long approvalId;
	private Byte supportType;
	private	Long formOriginId;
	private	String approvalName;

	public Long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

	public Byte getSupportType() {
		return supportType;
	}

	public void setSupportType(Byte supportType) {
		this.supportType = supportType;
	}

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public String getApprovalName() {
		return approvalName;
	}

	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
