package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>conditionNodeId: getFlowCaseDetailV2里的按钮上返回的conditionNodeId</li>
 * </ul>
 */
public class ListNextBranchesCommand {

    private Long flowCaseId;
    private Long conditionNodeId;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Long getConditionNodeId() {
        return conditionNodeId;
    }

    public void setConditionNodeId(Long conditionNodeId) {
        this.conditionNodeId = conditionNodeId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
