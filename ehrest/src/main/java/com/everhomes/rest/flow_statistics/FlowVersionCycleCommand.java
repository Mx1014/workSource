package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowMainId: flowMainId(必填)</li>
 * <li>flowVersion: 版本号(必填)</li>
 * <li>namespaceId: 域空间ID(必填)</li>
 * </ul>
 */
public class FlowVersionCycleCommand {
    private Long flowMainId;
    private Integer flowVersion; //版本号
    private Integer namespaceId; //域空间ID

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
