package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowCaseId: flowCaseId</li>
 * </ul>
 */
public class GetFlowCaseBriefCommand {

    @NotNull private Long flowCaseId;
    // private String flowUserType;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
