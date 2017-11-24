package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>hardwareId: 门禁设备的 MAC 地址</li>
 * <li>firwareVer: 门禁设备固件的版本号，比如 0.01 </li>
 * <li>rsaAclinkPub: 门禁激活的公钥 </li>
 * <li>ownerType: 门禁属于的小区或者企业， 0 : 小区， 1: 企业</li>
 * <li>ownerId: 门禁属于的对象ID</li>
 * <li>name: 门禁名字， 16字节以内英文</li>
 * <li>description: 描述</li>
 * <li>address: 地址信息</li>
 * <li>groupId: 分组信息</li>
 * </ul>
 * @author janson
 *
 */
public class DoorAccessActivingCommand {
    @NotNull
    String hardwareId;
    
    String firwareVer;
    
    @NotNull
    String rsaAclinkPub;
    
    @NotNull
    Byte ownerType;
    
    @NotNull
    Long ownerId;
    
    @NotNull
    String name;
    
    String displayName;
    
    Long groupId;
    
    String description;
    String address;
    
    public String getHardwareId() {
        return hardwareId;
    }
    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }
    public String getRsaAclinkPub() {
        return rsaAclinkPub;
    }
    public void setRsaAclinkPub(String rsaAclinkPub) {
        this.rsaAclinkPub = rsaAclinkPub;
    }
    
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
    public String getFirwareVer() {
        return firwareVer;
    }
    public void setFirwareVer(String firwareVer) {
        this.firwareVer = firwareVer;
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
    
    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
