package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;

import java.util.List;

/**
 * <ul>
 * <li>approvalId : 审批id</li>
 * <li>approvalGroupId : 审批所属组id</li>
 * <li>approvalName : 审批名称</li>
 * <li>approvalRemark : 审批备注</li>
 * <li>scopes : 可见范围 参考{@link com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO}</li>
 * </ul>
 * @author ryan
 *
 */
public class UpdateEnterpriseApprovalCommand {

    private Long approvalId;

    private Long approvalGroupId;

    private	String approvalName;

    private String approvalRemark;

    @ItemType(GeneralApprovalScopeMapDTO.class)
    private List<GeneralApprovalScopeMapDTO> scopes;

    public UpdateEnterpriseApprovalCommand() {
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public Long getApprovalGroupId() {
        return approvalGroupId;
    }

    public void setApprovalGroupId(Long approvalGroupId) {
        this.approvalGroupId = approvalGroupId;
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
}
