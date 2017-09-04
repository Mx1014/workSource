package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>nodes: 节点列表 {@link com.everhomes.rest.flow.FlowNodeDTO}</li>
 *     <li>links: 连接列表 {@link FlowLinkDTO}</li>
 *     <li>lanes: 泳道列表 {@link com.everhomes.rest.flow.FlowLaneDTO}</li>
 *     <li>conditions: conditions {@link com.everhomes.rest.flow.FlowConditionDTO}</li>
 * </ul>
 */
public class FlowGraphDTO {

    private Long flowId;

    @ItemType(FlowNodeDTO.class)
    private List<FlowNodeDTO> nodes;

    @ItemType(FlowLinkDTO.class)
    private List<FlowLinkDTO> links;

    @ItemType(FlowLaneDTO.class)
    private List<FlowLaneDTO> lanes;

    @ItemType(FlowConditionDTO.class)
    private List<FlowConditionDTO> conditions;

    public List<FlowNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<FlowNodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<FlowLinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<FlowLinkDTO> links) {
        this.links = links;
    }

    public List<FlowLaneDTO> getLanes() {
        return lanes;
    }

    public void setLanes(List<FlowLaneDTO> lanes) {
        this.lanes = lanes;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public List<FlowConditionDTO> getConditions() {
        return conditions;
    }

    public void setConditions(List<FlowConditionDTO> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
