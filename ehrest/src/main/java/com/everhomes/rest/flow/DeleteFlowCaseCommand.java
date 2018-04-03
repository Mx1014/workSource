// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowCaseId: flowCaseId</li>
 * </ul>
 */
public class DeleteFlowCaseCommand {

    private Long flowCaseId;

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
