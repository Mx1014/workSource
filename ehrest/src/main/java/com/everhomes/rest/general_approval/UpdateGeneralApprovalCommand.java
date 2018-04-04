package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>approvalId : 审批id</li>
 * <li>supportType: APP可用，WEB 可用，APP 与 WEB 都可用 {@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li> 
 * <li>approvalName : 审批名称</li>
 * <li>approvalRemark : 审批备注</li>
 * <li>scopes : 可见范围 参考{@link com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO}</li>
 * </ul>
 * @author janson
 *
 */
public class UpdateGeneralApprovalCommand {

	private Long approvalId;

	private Byte supportType;

//	private	Long formOriginId;

	private	String approvalName;

	//added by nan.rong for approval-2.0
	private String approvalRemark;

	@ItemType(GeneralApprovalScopeMapDTO.class)
	private List<GeneralApprovalScopeMapDTO> scopes;

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

	public String getApprovalName() {
		return approvalName;
	}

	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}

	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	public List<GeneralApprovalScopeMapDTO> getScopes() {
		return scopes;
	}

	public void setScopes(List<GeneralApprovalScopeMapDTO> scopes) {
		this.scopes = scopes;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
