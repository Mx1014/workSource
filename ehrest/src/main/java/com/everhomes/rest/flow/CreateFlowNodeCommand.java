// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>nodeName: nodeName</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>nodeLevel: nodeLevel</li>
 *     <li>params: params</li>
 *     <li>nodeType: nodeType</li>
 *     <li>processMode: processMode</li>
 *     <li>branchDecider: branchDecider</li>
 *     <li>conditions: conditions</li>
 * </ul>
 */
public class CreateFlowNodeCommand {
    private Integer namespaceId;
    private String nodeName;
    private Long flowMainId;
    private Integer nodeLevel;
    private String params;

    // ----------------------
    private Byte nodeType;
    private Byte processMode;
    private Byte branchDecider;

    private List conditions;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getNodeName() {
        return nodeName;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
