package com.everhomes.rest.flow_statistics;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowMainId: flowMainId(必填)</li>
 * <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class FindFlowVersionCommand {

    private Long  flowMainId ;
    private Integer namespaceId ;//域空间ID

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
