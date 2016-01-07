package com.everhomes.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class DoorAccessDTO {
    private Byte     status;
    private Long     activeUserId;
    private String     description;
    private Long     creatorUserId;
    private Long     aclinkId;
    private Byte     ownerType;
    private Double     longitude;
    private Timestamp     createTime;
    private Byte     doorType;
    private String     geohash;
    private String     avatar;
    private String     address;
    private Double     latitude;
    private Long     ownerId;
    private Byte     role;
    private Long     id;
    private String     name;

    

    public Byte getStatus() {
        return status;
    }



    public void setStatus(Byte status) {
        this.status = status;
    }



    public Long getActiveUserId() {
        return activeUserId;
    }



    public void setActiveUserId(Long activeUserId) {
        this.activeUserId = activeUserId;
    }



    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }



    public Long getCreatorUserId() {
        return creatorUserId;
    }



    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }



    public Long getAclinkId() {
        return aclinkId;
    }



    public void setAclinkId(Long aclinkId) {
        this.aclinkId = aclinkId;
    }



    public Byte getOwnerType() {
        return ownerType;
    }



    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }



    public Double getLongitude() {
        return longitude;
    }



    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



    public Timestamp getCreateTime() {
        return createTime;
    }



    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    public Byte getDoorType() {
        return doorType;
    }



    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }



    public String getGeohash() {
        return geohash;
    }



    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }



    public String getAvatar() {
        return avatar;
    }



    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }



    public String getAddress() {
        return address;
    }



    public void setAddress(String address) {
        this.address = address;
    }



    public Double getLatitude() {
        return latitude;
    }



    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }



    public Long getOwnerId() {
        return ownerId;
    }



    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }



    public Byte getRole() {
        return role;
    }



    public void setRole(Byte role) {
        this.role = role;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
