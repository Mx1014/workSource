// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>status: 门禁状态， 0 激活中，1 已激活，2 无效</li>
 * <li>activeUserId: 激活者id</li>
 * <li>description: 门禁描述</li>
 * <li>creatorUserId: 创建者id</li>
 * <li>ownerType: 门禁所属组织类型 {@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>longitude: 经度</li>
 * <li>latitude: 纬度</li>
 * <li>createTime: 创建时间</li>
 * <li>doorType: 门禁类型{@link com.everhomes.rest.aclink.DoorAccessType}</li>
 * <li>address: 门禁地址</li>
 * <li>ownerId:门禁所属组织的Id</li>
 * <li>role:default 0</li>
 * <li>id:门禁id</li>
 * <li>name: 门禁名字</li>
 * <li>displayName: 用户端显示的门禁名字</li>
 * <li>hardwareId: 门禁硬件mac地址</li>
 * <li>creatorName: 创建者名字</li>
 * <li>creatorPhone: 创建者手机号</li>
 * <li>linkStatus: 链接状态0已连接1未连接{@link com.everhomes.rest.aclink.DoorAccessLinkStatus}</li>
 * <li>version:版本</li>
 * <li>groupId:分组id</li>
 * <li>groupNmae:分组名称</li>
 * <li>enableAmount:门禁是否允许授权按次开门，1是0否{@link com.everhomes.rest.aclink.DoorAuthEnableAmount}</li>
 * </ul>
 * @author janson
 *
 */
public class LocalDoorAccessDTO {
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
    private Byte enableAmount;
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getHardwareId() {
		return hardwareId;
	}
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreatorPhone() {
		return creatorPhone;
	}
	public void setCreatorPhone(String creatorPhone) {
		this.creatorPhone = creatorPhone;
	}
	public Byte getLinkStatus() {
		return linkStatus;
	}
	public void setLinkStatus(Byte linkStatus) {
		this.linkStatus = linkStatus;
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
	public Byte getEnableAmount() {
		return enableAmount;
	}
	public void setEnableAmount(Byte enableAmount) {
		this.enableAmount = enableAmount;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
