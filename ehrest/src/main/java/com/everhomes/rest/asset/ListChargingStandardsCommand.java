//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>namespaceId:域空间</li>
 * <li>chargingItemId:收费项目id</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页大小</li>
 *</ul>
 */
public class ListChargingStandardsCommand {
    @NotNull
    private Long chargingItemId;
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    private Long pageAnchor;
    private Integer pageSize;

    private Integer namespaceId;


    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
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

    public ListChargingStandardsCommand() {

    }
}
