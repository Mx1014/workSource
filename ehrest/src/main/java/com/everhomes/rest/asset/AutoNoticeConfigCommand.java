//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/11/8.
 */

public class AutoNoticeConfigCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    @ItemType(Integer.class)
    private List<Integer> configDays;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Integer> getConfigDays() {
        return configDays;
    }

    public void setConfigDays(List<Integer> configDays) {
        this.configDays = configDays;
    }
}
