package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>needFlowButton: needFlowButton</li>
 *     <li>flowUserType: flowUserType</li>
 * </ul>
 */
public class GetFlowCaseDetailByIdCommand {

    private Long flowCaseId;
    private String flowUserType;
    private Byte needFlowButton;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Byte getNeedFlowButton() {
        return needFlowButton;
    }

    public void setNeedFlowButton(Byte needFlowButton) {
        this.needFlowButton = needFlowButton;
    }

    public String getFlowUserType() {
        return flowUserType;
    }

    public void setFlowUserType(String flowUserType) {
        this.flowUserType = flowUserType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
