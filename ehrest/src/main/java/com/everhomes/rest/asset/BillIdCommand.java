//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>ownerId:所属者ID</li>
 * <li>ownerType:所属者类型</li>
 * <li>billId:账单id</li>
 * <li>targetType:客户类型，个人eh_user;企业：eh_organization</li>
 *</ul>
 */
public class BillIdCommand {
    private String ownerType;
    private Long ownerId;
    private String billId;
    private String targetType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
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
