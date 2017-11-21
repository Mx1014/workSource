//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/15.
 */
/**
 *<ul>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>targetType:客户类型,sceneType为default，family时，类型为eh_user即个人，当sceneType为pm_admin屏蔽，当sceneType为其他，则类型为eh_organization即企业</li>
 * <li>targetId:客户id，客户类型为企业时，targetId为企业id</li>
 * <li>namespaceId:域空间</li>
 *</ul>
 */
public class ShowBillForClientV2Command {
    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;
    private Integer namespaceId;
    private Byte isPendingPayment;

    public Byte getIsPendingPayment() {
        return isPendingPayment;
    }

    public void setIsPendingPayment(Byte isPendingPayment) {
        this.isPendingPayment = isPendingPayment;
    }

    public Byte getIsOwed() {
        return isOwed;
    }

    public void setIsOwed(Byte isOwed) {
        this.isOwed = isOwed;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
