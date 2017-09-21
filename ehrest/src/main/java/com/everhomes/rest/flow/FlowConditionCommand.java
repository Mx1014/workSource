package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>flowNodeId: 当前节点id</li>
 *     <li>flowNodeLevel: 条件节点level</li>
 *     <li>nextNodeId: 条件成立时去向的节点id</li>
 *     <li>nextNodeLevel: 条件成立时去向的节点level</li>
 *     <li>conditionLevel: 条件level</li>
 *     <li>flowLinkLevel: 条件成立时走的link</li>
 *     <li>expressions: 条件表达式列表 {@link FlowConditionExpressionCommand}</li>
 * </ul>
 */
public class FlowConditionCommand {

    private Long flowNodeId;
    private Integer flowNodeLevel;
    private Integer conditionLevel;
    private Long nextNodeId;
    private Integer nextNodeLevel;
    private Integer flowLinkLevel;
    private Long flowLinkId;

    @ItemType(FlowConditionExpressionCommand.class)
    private List<FlowConditionExpressionCommand> expressions;

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public Integer getFlowNodeLevel() {
        return flowNodeLevel;
    }

    public void setFlowNodeLevel(Integer flowNodeLevel) {
        this.flowNodeLevel = flowNodeLevel;
    }

    public Long getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(Long nextNodeId) {
        this.nextNodeId = nextNodeId;
    }

    public Integer getNextNodeLevel() {
        return nextNodeLevel;
    }

    public void setNextNodeLevel(Integer nextNodeLevel) {
        this.nextNodeLevel = nextNodeLevel;
    }

    public List<FlowConditionExpressionCommand> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<FlowConditionExpressionCommand> expressions) {
        this.expressions = expressions;
    }

    public Integer getFlowLinkLevel() {
        return flowLinkLevel;
    }

    public void setFlowLinkLevel(Integer flowLinkLevel) {
        this.flowLinkLevel = flowLinkLevel;
    }

    public Long getFlowLinkId() {
        return flowLinkId;
    }

    public void setFlowLinkId(Long flowLinkId) {
        this.flowLinkId = flowLinkId;
    }

    public Integer getConditionLevel() {
        return conditionLevel;
    }

    public void setConditionLevel(Integer conditionLevel) {
        this.conditionLevel = conditionLevel;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
