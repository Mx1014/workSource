package com.everhomes.rest.acl.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 资源拥有者类型，如forum,system等</li>
 * <li>ownerId: 对应的id</li>
 * <li>targetType: 目标类型，如EhUsers,EhGroups等</li>
 * <li>targetId: 目标id，如userId,groupId</li>
 * <li>roleId: 角色id，参考{@link com.everhomes.rest.acl.RoleConstants}</li>
 * </ul>
 */
public class AssignUserRoleAdminCommand {

    @NotNull
    private String ownerType;
    private Long ownerId;
    @NotNull
    private String targetType;
    @NotNull
    private Long targetId;
    @NotNull
    private Long roleId;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
