package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>organizationId: 总公司ID</li>
 * <li>flowCaseId: 工作流ID</li>
 * <li>stepCount: stepCount</li>
 * <li>currentFlowNodeId: 操作按钮所属的节点</li>
 * <li>buttonId: 按钮ID</li>
 * <li>queryType: 查询节点关联表单的方式，参考{@link com.everhomes.rest.general_approval.QueryGeneralFormByFlowNodeType}</li>
 * </ul>
 */
public class GetGeneralFormsAndValuesByFlowNodeCommand {
    private Integer namespaceId;
    private Long organizationId;
    private Long flowCaseId;
    private Long currentFlowNodeId;
    private Long buttonId;
    private Long stepCount;
    private Byte queryType;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Long getCurrentFlowNodeId() {
        return currentFlowNodeId;
    }

    public void setCurrentFlowNodeId(Long currentFlowNodeId) {
        this.currentFlowNodeId = currentFlowNodeId;
    }

    public Byte getQueryType() {
        return queryType;
    }

    public void setQueryType(Byte queryType) {
        this.queryType = queryType;
    }

    public Long getStepCount() {
        return stepCount;
    }

    public void setStepCount(Long stepCount) {
        this.stepCount = stepCount;
    }

    public Long getButtonId() {
        return buttonId;
    }

    public void setButtonId(Long buttonId) {
        this.buttonId = buttonId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
