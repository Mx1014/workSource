//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/24.
 */

public class FunctionDisableListCommand {
    private Integer namespaceId;
    private String ownerType;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
