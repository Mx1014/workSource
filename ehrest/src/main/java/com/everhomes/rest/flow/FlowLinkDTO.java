package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>linkLevel: 连接level</li>
 *     <li>fromNodeId: 起始节点id</li>
 *     <li>fromNodeLevel: 起始节点level</li>
 *     <li>toNodeId: 结束节点id</li>
 *     <li>toNodeLevel: 结束节点level</li>
 * </ul>
 */
public class FlowLinkDTO {

    private Long id;
    private Integer namespaceId;
    private Long flowMainId;
    private Integer flowVersion;
    private Integer linkLevel;
    private Long fromNodeId;
    private Integer fromNodeLevel;
    private Long toNodeId;
    private Integer toNodeLevel;

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

    public Long getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(Long fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public Long getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(Long toNodeId) {
        this.toNodeId = toNodeId;
    }

    public Integer getFromNodeLevel() {
        return fromNodeLevel;
    }

    public void setFromNodeLevel(Integer fromNodeLevel) {
        this.fromNodeLevel = fromNodeLevel;
    }

    public Integer getToNodeLevel() {
        return toNodeLevel;
    }

    public void setToNodeLevel(Integer toNodeLevel) {
        this.toNodeLevel = toNodeLevel;
    }

    public Integer getLinkLevel() {
        return linkLevel;
    }

    public void setLinkLevel(Integer linkLevel) {
        this.linkLevel = linkLevel;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
