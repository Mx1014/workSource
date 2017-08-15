//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>targetType:客户类型,sceneType为default，family时，类型为eh_user即个人，当sceneType为pm_admin屏蔽，当sceneType为其他，则类型为eh_organization即企业</li>
 * <li>targetId:客户id，客户类型为企业时，targetId为企业id</li>
 * <li>billGroupId:账单组id</li>
 * <li>isOnlyOwedBill:是否只显示待缴账单1:是;0：不是</li>
 *</ul>
 */
public class ClientIdentityCommand {
    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;
    private Long billGroupId;
    private Byte isOnlyOwedBill;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getOwnerType() {
        return ownerType;
    }

    public Byte getIsOnlyOwedBill() {
        return isOnlyOwedBill;
    }

    public void setIsOnlyOwedBill(Byte isOnlyOwedBill) {
        this.isOnlyOwedBill = isOnlyOwedBill;
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

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public ClientIdentityCommand() {

    }
}
