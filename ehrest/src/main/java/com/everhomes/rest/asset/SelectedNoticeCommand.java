//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billIds:账单id集合</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 *</ul>
 */
public class SelectedNoticeCommand {
    @ItemType(Long.class)
    private List<Long> billIds;
    private String ownerType;
    private Long ownerId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<Long> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
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

    public SelectedNoticeCommand() {

    }
}
