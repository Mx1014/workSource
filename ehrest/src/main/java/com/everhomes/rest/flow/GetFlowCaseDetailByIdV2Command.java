package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowCaseId: 任务id</li>
 *     <li>flowUserType: 用户类型{@link com.everhomes.rest.flow.FlowUserType} processor: 处理者， node_applier: 申请者</li>
 * </ul>
 */
public class GetFlowCaseDetailByIdV2Command {

    private Long flowCaseId;
    private String flowUserType;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
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
