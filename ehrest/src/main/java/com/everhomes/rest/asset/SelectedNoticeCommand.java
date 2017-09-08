//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billIdAndTypes:账单id客户类型的集合</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 *</ul>
 */
public class SelectedNoticeCommand {
    @ItemType(Long.class)
    private List<BillIdAndType> billIdAndTypes;
    private String ownerType;
    private Long ownerId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<BillIdAndType> getBillIdAndTypes() {
        return billIdAndTypes;
    }

    public void setBillIdAndTypes(List<BillIdAndType> billIdAndTypes) {
        this.billIdAndTypes = billIdAndTypes;
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
