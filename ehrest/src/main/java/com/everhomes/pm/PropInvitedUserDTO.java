// @formatter:off
package com.everhomes.pm;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>ownerUid: 邀请者id</li>
 * <li>inviteCode: 邀请码</li>
 * <li>inviteType: 邀请类型</li>
 * <li>expiration: 描述</li>
 * <li>targetEntityType: 目标实体类型</li>
 * <li>targetEntityId: 目标实体id</li>
 * <li>maxInviteCount: 最大邀请数量</li>
 * <li>currentInviteCount: 已邀请数量</li>
 * <li>status: 状态</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class PropInvitedUserDTO {
	private Long id;
    private Long ownerUid;
    private String inviteCode;
    private Byte inviteType;
    private Timestamp expiration;
    private String targetEntityType;
    private Long targetEntityId;
    private Integer maxInviteCount;
    private Integer currentInviteCount;
    private Byte status;
    private Timestamp createTime;
    
    public PropInvitedUserDTO() {
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerUid() {
		return ownerUid;
	}

	public void setOwnerUid(Long ownerUid) {
		this.ownerUid = ownerUid;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public Byte getInviteType() {
		return inviteType;
	}

	public void setInviteType(Byte inviteType) {
		this.inviteType = inviteType;
	}

	public Timestamp getExpiration() {
		return expiration;
	}

	public void setExpiration(Timestamp expiration) {
		this.expiration = expiration;
	}

	public String getTargetEntityType() {
		return targetEntityType;
	}

	public void setTargetEntityType(String targetEntityType) {
		this.targetEntityType = targetEntityType;
	}

	public Long getTargetEntityId() {
		return targetEntityId;
	}

	public void setTargetEntityId(Long targetEntityId) {
		this.targetEntityId = targetEntityId;
	}

	public Integer getMaxInviteCount() {
		return maxInviteCount;
	}

	public void setMaxInviteCount(Integer maxInviteCount) {
		this.maxInviteCount = maxInviteCount;
	}

	public Integer getCurrentInviteCount() {
		return currentInviteCount;
	}

	public void setCurrentInviteCount(Integer currentInviteCount) {
		this.currentInviteCount = currentInviteCount;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
