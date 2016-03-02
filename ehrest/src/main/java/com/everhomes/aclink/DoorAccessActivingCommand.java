package com.everhomes.aclink;

import javax.validation.constraints.NotNull;

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
    
}
