package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>name: 门禁名字</li>
 * <li>displayName: 用户端显示的门禁名字</li>
 * <li>hardwareId: 门禁硬件地址</li>
 * <li>status: 门禁状态， 0 激活中，1 已激活，2 无效</li>
 * <li>description: 门禁描述</li>
 * <li>address: 门禁地址</li>
 * <li>creator: 激活人员名字</li>
 * </ul>
 * @author janson
 *
 */
public class DoorAccessDTO {
    private Byte     status;
    private Long     activeUserId;
    private String     description;
    private Long     creatorUserId;
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
    private String displayName;
    private String hardwareId;
    private String creatorName;
    private String creatorPhone;
    private Byte linkStatus;
    private String  version;
    private String groupName;
    private Long groupId;
    

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


    public String getHardwareId() {
        return hardwareId;
    }



    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
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



    public String getCreatorName() {
        return creatorName;
    }



    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Byte getLinkStatus() {
        return linkStatus;
    }



    public void setLinkStatus(Byte linkStatus) {
        this.linkStatus = linkStatus;
    }

    public String getCreatorPhone() {
        return creatorPhone;
    }



    public void setCreatorPhone(String creatorPhone) {
        this.creatorPhone = creatorPhone;
    }



    public String getVersion() {
        return version;
    }



    public void setVersion(String version) {
        this.version = version;
    }



    public String getGroupName() {
        return groupName;
    }



    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
