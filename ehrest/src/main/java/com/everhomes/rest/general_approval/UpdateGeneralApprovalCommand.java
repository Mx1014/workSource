package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>approvalId : id</li> 
 * <li>supportType: APP可用，WEB 可用，APP 与 WEB 都可用 {@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li> 
 * <li>formOriginId: 原始 formId，如果修改了版本，则原始的数据保留</li>
 * <li>approvalName : 审批名称</li> 
 * </ul>
 * @author janson
 *
 */
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
