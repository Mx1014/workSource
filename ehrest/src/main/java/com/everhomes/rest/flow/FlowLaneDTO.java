package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>displayName: 泳道名称</li>
 *     <li>displayNameAbsort: 终止状态下的泳道名称</li>
 *     <li>laneLevel: 泳道level</li>
 *     <li>flowNodeLevel: 第一个节点level</li>
 *     <li>identifierNodeLevel: 申请人按钮节点level</li>
 *     <li>identifierNodeId: 申请人按钮节点id</li>
 * </ul>
 */
public class FlowLaneDTO {

    private Long id;
    private Integer namespaceId;
    private Long flowMainId;
    private Integer flowVersion;
    private String displayName;
    private String displayNameAbsort;
    private Integer laneLevel;
    private Integer flowNodeLevel;
    private Integer identifierNodeLevel;
    private Long identifierNodeId;

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

    public Integer getIdentifierNodeLevel() {
        return identifierNodeLevel;
    }

    public void setIdentifierNodeLevel(Integer identifierNodeLevel) {
        this.identifierNodeLevel = identifierNodeLevel;
    }

    public Long getIdentifierNodeId() {
        return identifierNodeId;
    }

    public void setIdentifierNodeId(Long identifierNodeId) {
        this.identifierNodeId = identifierNodeId;
    }

    public String getDisplayNameAbsort() {
        return displayNameAbsort;
    }

    public void setDisplayNameAbsort(String displayNameAbsort) {
        this.displayNameAbsort = displayNameAbsort;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
