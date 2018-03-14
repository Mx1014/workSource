// @formatter:off
package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/1/20 16:36
 */
public class QueryCitiesCommand {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
