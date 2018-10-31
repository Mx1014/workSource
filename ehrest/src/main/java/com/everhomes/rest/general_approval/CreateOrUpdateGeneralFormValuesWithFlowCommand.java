package com.everhomes.rest.general_approval;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>flowCaseId: 工作流id</li>
 * <li>flowNodeId: 当前触发按钮所在的节点ID</li>
 * <li>buttonId: 按钮id</li>
 * <li>stepCount: 当前节点的 stepCount</li>
 * <li>postGeneralFormValCommands: 表单请求项,参考{@link com.everhomes.rest.general_approval.PostGeneralFormValCommand}</li>
 * </ul>
 */
public class CreateOrUpdateGeneralFormValuesWithFlowCommand {
    private Long organizationId;
    private Long flowCaseId;
    private Long flowNodeId;
    private Long buttonId;
    private Long stepCount;
    private List<PostGeneralFormValCommand> postGeneralFormValCommands;

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

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public Long getButtonId() {
        return buttonId;
    }

    public void setButtonId(Long buttonId) {
        this.buttonId = buttonId;
    }

    public List<PostGeneralFormValCommand> getPostGeneralFormValCommands() {
        return postGeneralFormValCommands;
    }

    public void setPostGeneralFormValCommands(List<PostGeneralFormValCommand> postGeneralFormValCommands) {
        this.postGeneralFormValCommands = postGeneralFormValCommands;
    }

    public Long getStepCount() {
        return stepCount;
    }

    public void setStepCount(Long stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
