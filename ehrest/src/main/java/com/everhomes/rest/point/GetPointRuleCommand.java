package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: 积分系统id</li>
 *     <li>eventName: 事件名称</li>
 * </ul>
 */
public class GetPointRuleCommand {
    @NotNull
    private Integer namespaceId;
    @NotNull
    private Long systemId;
    @NotNull
    private String eventName;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
