package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class DoorAuthDTO {
    private Byte     status;
    private Byte     authType;
    private Long     validFromMs;
    private Long     userId;
    private Timestamp     createTime;
    private Long     validEndMs;
    private Long     ownerId;
    private Long     doorId;
    private Long     approveUserId;
    private Long     id;
    private Byte     ownerType;
    private String doorName;
    private String hardwareId;
    private String   nickname;
    private String   phone;
    private String approveUserName;
    private String   organization;
    private String   description;
    private String address;
    private String authMethod;

    private Long goFloor;


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public Byte getAuthType() {
        return authType;
    }


    public void setAuthType(Byte authType) {
        this.authType = authType;
    }


    public Long getValidFromMs() {
        return validFromMs;
    }


    public void setValidFromMs(Long validFromMs) {
        this.validFromMs = validFromMs;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public Long getValidEndMs() {
        return validEndMs;
    }


    public void setValidEndMs(Long validEndMs) {
        this.validEndMs = validEndMs;
    }


    public Long getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Long getDoorId() {
        return doorId;
    }


    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }


    public Long getApproveUserId() {
        return approveUserId;
    }


    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Byte getOwnerType() {
        return ownerType;
    }


    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    

    public String getDoorName() {
        return doorName;
    }


    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }


    public String getHardwareId() {
        return hardwareId;
    }


    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }
    
    

    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getApproveUserName() {
        return approveUserName;
    }


    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }
    
    


    public String getOrganization() {
        return organization;
    }


    public void setOrganization(String organization) {
        this.organization = organization;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getAuthMethod() {
        return authMethod;
    }


    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    public Long getGoFloor() {
        return goFloor;
    }

    public void setGoFloor(Long goFloor) {
        this.goFloor = goFloor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
