package com.everhomes.aclink;

public class DoorAccessActivingCommand {
    Long uid;
    
    String hardwareId;
    String firwareVer;
    
    String rsaAclinkPub;
    DoorAccessOwnerType ownerType;
    Long ownerId;
    
    public Long getUid() {
        return uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
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
    public DoorAccessOwnerType getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(DoorAccessOwnerType ownerType) {
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
}
