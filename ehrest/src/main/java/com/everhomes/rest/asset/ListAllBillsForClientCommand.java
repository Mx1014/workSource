//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/21.
 */

public class ListAllBillsForClientCommand {
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private String targetType;
    private Long targetId;
    private Byte isOnlyOwedBill;
    private Long billGroupId;

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Byte getIsOnlyOwedBill() {
        return isOnlyOwedBill;
    }

    public void setIsOnlyOwedBill(Byte isOnlyOwedBill) {
        this.isOnlyOwedBill = isOnlyOwedBill;
    }

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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
