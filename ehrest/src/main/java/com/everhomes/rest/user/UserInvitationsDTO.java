package com.everhomes.rest.user;

/**
 * 
 * @author elians
 * <ul>
 * <li>id:邀请码ID</li>
 * <li>ownerUid:邀请码拥有者</li>
 * <li>inviteCode:邀请码</li>
 * <li>inviteType:邀请类型</li>
 * <li>expiration:超时时间</li>
 * <li>targetEntityType:实体类型</li>
 * <li>targetEntityId:实体ID</li>
 * <li>maxInviteCount:最大邀请数</li>
 * <li>currentInviteCount：当前邀请数</li>
 * <li>status：状态</li>
 * <li>createTime:创建时间</li>
 * </ul>
 *
 */
public class UserInvitationsDTO {
    private java.lang.Long id;
    private java.lang.Long ownerUid;
    private java.lang.String inviteCode;
    private java.lang.Byte inviteType;
    private java.sql.Timestamp expiration;
    private java.lang.String targetEntityType;
    private java.lang.Long targetEntityId;
    private java.lang.Integer maxInviteCount;
    private java.lang.Integer currentInviteCount;
    private java.lang.Byte status;
    private java.sql.Timestamp createTime;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(java.lang.Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public java.lang.String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(java.lang.String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public java.lang.Byte getInviteType() {
        return inviteType;
    }

    public void setInviteType(java.lang.Byte inviteType) {
        this.inviteType = inviteType;
    }

    public java.sql.Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(java.sql.Timestamp expiration) {
        this.expiration = expiration;
    }

    public java.lang.String getTargetEntityType() {
        return targetEntityType;
    }

    public void setTargetEntityType(java.lang.String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public java.lang.Long getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(java.lang.Long targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public java.lang.Integer getMaxInviteCount() {
        return maxInviteCount;
    }

    public void setMaxInviteCount(java.lang.Integer maxInviteCount) {
        this.maxInviteCount = maxInviteCount;
    }

    public java.lang.Integer getCurrentInviteCount() {
        return currentInviteCount;
    }

    public void setCurrentInviteCount(java.lang.Integer currentInviteCount) {
        this.currentInviteCount = currentInviteCount;
    }

    public java.lang.Byte getStatus() {
        return status;
    }

    public void setStatus(java.lang.Byte status) {
        this.status = status;
    }

    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }
}
