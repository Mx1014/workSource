package com.everhomes.rest.acl.admin;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class AclRoleAssignmentsDTO {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;
    private String targetName;
    private Long roleId;
    private Long creatorUid;
    private Timestamp createTime;
    
    private String roleName;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getTargetName() {
        return targetName;
    }
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public Long getCreatorUid() {
        return creatorUid;
    }
    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    
    public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
