// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>nodeId: 节点id</li>
 *     <li>stepType: 按钮类型 {@link com.everhomes.rest.flow.FlowStepType}</li>
 *     <li>userType: 用户类型 {@link com.everhomes.rest.flow.FlowUserType}</li>
 *     <li>defaultOrder: 排序</li>
 * </ul>
 */
public class CreateFlowButtonCommand {

    @NotNull
    private Long flowId;
    @NotNull
    private Long nodeId;
    @NotNull
    private String stepType;
    @NotNull
    private String userType;
    private Integer defaultOrder;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
