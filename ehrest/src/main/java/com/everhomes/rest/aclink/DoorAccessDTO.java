// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>id:门禁id</li>
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
 * <li>enableDuration:门禁是否允许授权按时间开门，1是0否</li>
 * <li>server：门禁关联服务器{@link com.everhomes.rest.aclink.AclinkServerDTO}</li>
 * <li>recDevices:门禁关联人脸识别设备(ipad,摄像头,前端都算作摄像头){@link com.everhomes.rest.aclink.AclinkServerRelDTO}</li>
 * <li>hasQr:门禁是否支持二维码0否1是</li>
 * <li>maxDuration:访客授权最长有效期</li>
 * <li>maxCount:访客授权最大次数</li>
 * <li>defualtInvalidDuration:按次开门授权默认有效期</li>
 * <li>useCustomAuthConfig:是否启用自定义授权规则</li>
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
    private Byte enableAmount;
    private Byte enableDuration;
    private AclinkServerDTO server;
    private List<AclinkServerRelDTO> recDevices;
    private String localUUid;
    private String localAesKey;
    private Long localServerId;
    private Byte hasQr;
    private Integer maxDuration;
    private Integer maxCount;
    private Byte useCustomAuthConfig;
    private Integer defualtInvalidDuration;
    private String organizationName;
    private String addressDetail;
    private String communityName;
    private String buildingName;
    private String floorName;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public Byte getUseCustomAuthConfig() {
		return useCustomAuthConfig;
	}



	public void setUseCustomAuthConfig(Byte useCustomAuthConfig) {
		this.useCustomAuthConfig = useCustomAuthConfig;
	}



	public List<AclinkServerRelDTO> getRecDevices() {
		return recDevices;
	}



	public void setRecDevices(List<AclinkServerRelDTO> recDevices) {
		this.recDevices = recDevices;
	}



	public Byte getEnableDuration() {
		return enableDuration;
	}



	public void setEnableDuration(Byte enableDuration) {
		this.enableDuration = enableDuration;
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



	public String getLocalAesKey() {
		return localAesKey;
	}



	public void setLocalAesKey(String localAesKey) {
		this.localAesKey = localAesKey;
	}



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



	public Byte getEnableAmount() {
		return enableAmount;
	}



	public void setEnableAmount(Byte enableAmount) {
		this.enableAmount = enableAmount;
	}



	public AclinkServerDTO getServer() {
		return server;
	}



	public void setServer(AclinkServerDTO server) {
		this.server = server;
	}



	public String getLocalUUid() {
		return localUUid;
	}



	public void setLocalUUid(String localUUid) {
		this.localUUid = localUUid;
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



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
