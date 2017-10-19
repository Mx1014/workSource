// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 创建时不传，更新时传</li>
 *     <li>nodeName: 节点名称</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>nodeLevel: nodeLevel</li>
 *     <li>params: 节点参数</li>
 *     <li>nodeType: 节点类型{@link com.everhomes.rest.flow.FlowNodeType}</li>
 *     <li>flowLaneLevel: 泳道level</li>
 *     <li>flowLaneId: flowLaneId</li>
 *     <li>branch: 分支信息条件节点有这个参数 {@link FlowBranchCommand}</li>
 * </ul>
 */
public class CreateFlowNodeCommand {

    private Long id;
    private Integer namespaceId;
    private String nodeName;
    private Long flowMainId;
    private Integer nodeLevel;
    private String params;
    private String nodeType;

    private Integer flowLaneLevel;
    private Long flowLaneId;

    private FlowBranchCommand branch;

    public String getNodeName() {
        return nodeName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Integer getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getFlowLaneLevel() {
        return flowLaneLevel;
    }

    public void setFlowLaneLevel(Integer flowLaneLevel) {
        this.flowLaneLevel = flowLaneLevel;
    }

    public Long getFlowLaneId() {
        return flowLaneId;
    }

    public void setFlowLaneId(Long flowLaneId) {
        this.flowLaneId = flowLaneId;
    }

    public FlowBranchCommand getBranch() {
        return branch;
    }

    public void setBranch(FlowBranchCommand branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}