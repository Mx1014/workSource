// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>newFlowName: 工作流名称</li>
 *     <li>newFlowDesc: 工作流描述</li>
 * </ul>
 */
public class UpdateFlowNameCommand {

    private Long flowId;
    private String newFlowName;
    private String newFlowDesc;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getNewFlowName() {
        return newFlowName;
    }

    public void setNewFlowName(String newFlowName) {
        this.newFlowName = newFlowName;
    }

    public String getNewFlowDesc() {
        return newFlowDesc;
    }

    public void setNewFlowDesc(String newFlowDesc) {
        this.newFlowDesc = newFlowDesc;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
