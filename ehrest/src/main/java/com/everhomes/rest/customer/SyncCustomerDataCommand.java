package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/5/30 14 :21
 */

public class SyncCustomerDataCommand {
    private Integer namespaceId;

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
