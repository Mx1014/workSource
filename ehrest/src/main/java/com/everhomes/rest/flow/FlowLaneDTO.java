package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>displayName: 泳道名称</li>
 *     <li>laneLevel: 泳道level</li>
 *     <li>flowNodeLevel: 第一个节点level</li>
 * </ul>
 */
public class FlowLaneDTO {

    private Long id;
    private Integer namespaceId;
    private Long flowMainId;
    private Integer flowVersion;
    private String displayName;
    private Integer laneLevel;
    private Integer flowNodeLevel;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getLaneLevel() {
        return laneLevel;
    }

    public void setLaneLevel(Integer laneLevel) {
        this.laneLevel = laneLevel;
    }

    public Integer getFlowNodeLevel() {
        return flowNodeLevel;
    }

    public void setFlowNodeLevel(Integer flowNodeLevel) {
        this.flowNodeLevel = flowNodeLevel;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
