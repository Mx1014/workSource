package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 创建 LingLing 门禁设备
 * <li>ownerType: 参考{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>ownerId: 属于的对象 ID</li>
 * <li>name: 名字</li>
 * <li>description: 描述</li>
 * <li>address: 所在地址</li>
 * <li>hardwareId: 硬件地址，lingling 的 设备 sn 号 </li>
 * <li>doorGroupId: 门禁分组ID，没有ID 表示为设备独立分组 </li>
 * </ul>
 * @author janson
 *
 */
public class CreateDoorAccessLingLing {
    @NotNull
    Byte ownerType;
    
    @NotNull
    Long ownerId;
    
    @NotNull
    String name;
    
    @NotNull
    String hardwareId;
    
    Long existsId;
    
    Long doorGroupId;
    
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

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public Long getDoorGroupId() {
        return doorGroupId;
    }

    public void setDoorGroupId(Long doorGroupId) {
        this.doorGroupId = doorGroupId;
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
    
    public Long getExistsId() {
        return existsId;
    }

    public void setExistsId(Long existsId) {
        this.existsId = existsId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
