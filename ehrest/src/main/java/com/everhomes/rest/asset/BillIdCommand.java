//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>ownerId:所属者ID</li>
 * <li>ownerType:所属者类型</li>
 * <li>billId:账单id</li>
 *</ul>
 */
public class BillIdCommand {
    private String ownerType;
    private Long ownerId;
    private Long billId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
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

    public BillIdCommand() {

    }
}
