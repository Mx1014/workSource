package com.everhomes.rest.decoration;

import com.everhomes.rest.flow.FlowCaseEntity;

import java.util.List;

/**
 * <ul>
 * <li>status：审批状态 参考{@link com.everhomes.rest.flow.FlowCaseStatus}</li>
 * <li>flowCaseForm: 表单数据 参考{@link com.everhomes.rest.flow.FlowCaseEntity}</li>
 * <li>approvalName</li>
 * <li>flowCaseId</li>
 * </ul>
 */
public class DecorationFlowCaseDTO {

    private String approvalName;
    private Long flowCaseId;
    private Byte status;
    private List<FlowCaseEntity> flowCaseForm;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<FlowCaseEntity> getFlowCaseForm() {
        return flowCaseForm;
    }

    public void setFlowCaseForm(List<FlowCaseEntity> flowCaseForm) {
        this.flowCaseForm = flowCaseForm;
    }
}
