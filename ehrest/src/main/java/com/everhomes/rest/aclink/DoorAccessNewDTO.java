// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>defualtInvalidDuration:按次开门授权默认有效期</li>
 * </ul>
 * @author janson
 *
 */
public class DoorAccessNewDTO {
    private Long id;
    private Integer namespaceId;
    private String uuid;
    private Byte doorType;
    private String hardwareId;
    private String name;
    private String displayName;
    private String description;
    private String avatar;
    private String address;
    private Long activeUserId;
    private Long creatorUserId;
    private Double longitude;
    private Double latitude;
    private String geohash;
    private String aesIv;
    private Byte linkStatus;
    private Byte ownerType;
    private Long ownerId;
    private Byte role;
    private Timestamp createTime;
    private Byte status;
    private Integer ackingSecretVersion;
    private Integer expectSecretKey;
    private Long groupid;
    private String floorId;
    private String macCopy;
    private Byte enableAmount;
    private Long localServerId;
    private Byte hasQr;
    private Integer maxDuration;
    private Integer maxCount;
    private Integer defualtInvalidDuration;
    private Byte enableDuration;
    private Long firmwareId;
    private String firmwareName;
    private Long deviceId;
    private String deviceName;
    private Integer cityId;
    private String province;

    private String namespaceName;
    private String communityName;
    private String organizationName;
    private String creatorUserName;

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Byte getDoorType() {
        return doorType;
    }

    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(Long activeUserId) {
        this.activeUserId = activeUserId;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getAesIv() {
        return aesIv;
    }

    public void setAesIv(String aesIv) {
        this.aesIv = aesIv;
    }

    public Byte getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(Byte linkStatus) {
        this.linkStatus = linkStatus;
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

    public Byte getRole() {
        return role;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getAckingSecretVersion() {
        return ackingSecretVersion;
    }

    public void setAckingSecretVersion(Integer ackingSecretVersion) {
        this.ackingSecretVersion = ackingSecretVersion;
    }

    public Integer getExpectSecretKey() {
        return expectSecretKey;
    }

    public void setExpectSecretKey(Integer expectSecretKey) {
        this.expectSecretKey = expectSecretKey;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getMacCopy() {
        return macCopy;
    }

    public void setMacCopy(String macCopy) {
        this.macCopy = macCopy;
    }

    public Byte getEnableAmount() {
        return enableAmount;
    }

    public void setEnableAmount(Byte enableAmount) {
        this.enableAmount = enableAmount;
    }

    public Long getLocalServerId() {
        return localServerId;
    }

    public void setLocalServerId(Long localServerId) {
        this.localServerId = localServerId;
    }

    public Byte getHasQr() {
        return hasQr;
    }

    public void setHasQr(Byte hasQr) {
        this.hasQr = hasQr;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getDefualtInvalidDuration() {
        return defualtInvalidDuration;
    }

    public void setDefualtInvalidDuration(Integer defualtInvalidDuration) {
        this.defualtInvalidDuration = defualtInvalidDuration;
    }

    public Byte getEnableDuration() {
        return enableDuration;
    }

    public void setEnableDuration(Byte enableDuration) {
        this.enableDuration = enableDuration;
    }

    public Long getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(Long firmwareId) {
        this.firmwareId = firmwareId;
    }

    public String getFirmwareName() {
        return firmwareName;
    }

    public void setFirmwareName(String firmwareName) {
        this.firmwareName = firmwareName;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
