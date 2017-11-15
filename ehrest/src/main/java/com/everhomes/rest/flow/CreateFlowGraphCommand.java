// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>validationStatus: validationStatus {@link com.everhomes.rest.flow.FlowValidationStatus}</li>
 *     <li>nodes: 节点列表 {@link com.everhomes.rest.flow.CreateFlowNodeCommand}</li>
 *     <li>links: 连接列表 {@link FlowLinkDTO}</li>
 *     <li>lanes: 泳道列表 {@link com.everhomes.rest.flow.FlowLaneDTO}</li>
 *     <li>conditions: 如果branchDecider是按照条件判断则是条件列表 {@link FlowConditionCommand}</li>
 * </ul>
 */
public class CreateFlowGraphCommand {

    @NotNull
    private Long flowId;
    @NotNull
    private Byte validationStatus;

    @ItemType(CreateFlowNodeCommand.class)
    @NotNull
    private List<CreateFlowNodeCommand> nodes;

    @ItemType(FlowLinkCommand.class)
    @NotNull
    private List<FlowLinkCommand> links;

    @ItemType(FlowLaneCommand.class)
    @NotNull
    private List<FlowLaneCommand> lanes;

    @ItemType(FlowConditionCommand.class)
    private List<FlowConditionCommand> conditions;

    public List<CreateFlowNodeCommand> getNodes() {
        return nodes;
    }

    public void setNodes(List<CreateFlowNodeCommand> nodes) {
        this.nodes = nodes;
    }

    public List<FlowLinkCommand> getLinks() {
        return links;
    }

    public void setLinks(List<FlowLinkCommand> links) {
        this.links = links;
    }

    public List<FlowLaneCommand> getLanes() {
        return lanes;
    }

    public void setLanes(List<FlowLaneCommand> lanes) {
        this.lanes = lanes;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public List<FlowConditionCommand> getConditions() {
        return conditions;
    }

    public void setConditions(List<FlowConditionCommand> conditions) {
        this.conditions = conditions;
    }

    public Byte getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(Byte validationStatus) {
        this.validationStatus = validationStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
