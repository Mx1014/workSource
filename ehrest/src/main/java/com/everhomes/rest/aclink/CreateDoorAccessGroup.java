package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 创建分组，比如大堂门禁，可以对应多台具体的门禁设备。一个独立的门禁也可以与自己产生一组。如果要把一个组里的门禁迁移到另外的组，则需要重置门禁，再重新激活或加入。
 * <li>ownerType: 参考{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>ownerId: 属于的对象 ID</li>
 * <li>name: 分组名字</li>
 * <li>description: 分组描述</li>
 * <li>address: 分组所在地址</li>
 * <li>groupType: 5 左邻分组， 6 令令分组</li>
 * </ul>
 * @author janson
 *
 */
public class CreateDoorAccessGroup {    
    @NotNull
    Byte ownerType;
    
    @NotNull
    Long ownerId;
    
    @NotNull
    String name;
    
    @NotNull
    Byte groupType;
    
    String   avatar;
    
    String description;
    
    String address;

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Byte getGroupType() {
        return groupType;
    }

    public void setGroupType(Byte groupType) {
        this.groupType = groupType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
