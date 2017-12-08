package com.everhomes.rest.blacklist;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerType: 范围类型 EhOrganizations，EhCommunities..</li>
 * <li>ownerId: 范围id</li>
 * <li>userId: 用户id</li>
 * <li>phone: 如果此用户不存在，则使用此接口创建 UserBlackList </li>
 * <li>privilegeIds: 权限集</li>
 * </ul>
 */
public class AddUserBlacklistCommand {

    private String ownerType;

    private Long ownerId;

    private Long userId;
    
    private String phone;

    @ItemType(Long.class)
    private List<Long> privilegeIds;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(List<Long> privilegeIds) {
        this.privilegeIds = privilegeIds;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
