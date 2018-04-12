//@formatter:off
package com.everhomes.rest.requisition;

/**
 * Created by Wentian Wang on 2018/1/20.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>namespaceId:域名id</li>
 *</ul>
 */
public class ListRequisitionTypesCommand {
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
