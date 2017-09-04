package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>conditionLevel: 条件level</li>
 *     <li>belongEntity: belongEntity</li>
 *     <li>belongTo: belongTo</li>
 *     <li>flowLinkId: flowLinkId</li>
 *     <li>flowLinkLevel: flowLinkLevel</li>
 *     <li>nextNodeId: 条件成立时执行的节点id</li>
 *     <li>nextNodeLevel: 条件成立时执行的节点level</li>
 *     <li>expressions: 条件表达式列表 {@link com.everhomes.rest.flow.FlowConditionExpressionDTO}</li>
 * </ul>
 */
public class FlowConditionDTO {

    private Long id;
    private Integer namespaceId;
    private Long flowMainId;
    private Integer flowVersion;
    private Integer conditionLevel;
    private String belongEntity;
    private Long belongTo;
    private Long flowLinkId;
    private Integer flowLinkLevel;
    private Long nextNodeId;
    private Integer nextNodeLevel;

    @ItemType(FlowConditionExpressionDTO.class)
    private List<FlowConditionExpressionDTO> expressions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
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

    public List<FlowConditionExpressionDTO> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<FlowConditionExpressionDTO> expressions) {
        this.expressions = expressions;
    }

    public Long getFlowLinkId() {
        return flowLinkId;
    }

    public void setFlowLinkId(Long flowLinkId) {
        this.flowLinkId = flowLinkId;
    }

    public Integer getFlowLinkLevel() {
        return flowLinkLevel;
    }

    public void setFlowLinkLevel(Integer flowLinkLevel) {
        this.flowLinkLevel = flowLinkLevel;
    }

    public Long getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Long belongTo) {
        this.belongTo = belongTo;
    }

    public String getBelongEntity() {
        return belongEntity;
    }

    public void setBelongEntity(String belongEntity) {
        this.belongEntity = belongEntity;
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
