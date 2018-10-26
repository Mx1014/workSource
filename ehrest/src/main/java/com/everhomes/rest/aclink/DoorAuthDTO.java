// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>id: id</li>
 * <li>status: 授权状态， 0 失效，1 有效</li>
 * <li>authType:授权类型，{@link com.everhomes.rest.aclink.DoorAuthType}</li>
 * <li>validFromMs: 有效期开始时间</li>
 * <li>userId: 访客id</li>
 * <li>createTime: 创建时间</li>
 * <li>validEndMs: 有效期终止时间</li>
 * <li>doorId: 门禁ID</li>
 * <li>ownerId: 门禁所属的ID</li>
 * <li>approveUserId:授权主体id</li>
 * <li>ownerType:门禁所属的类型</li>
 * <li>doorName:门禁名称</li>
 * <li>hardwareId：门禁mac地址</li>
 * <li>nickname:访客姓名</li>
 * <li>phone:访客手机号</li>
 * <li>approveUserName:授权主体名称</li>
 * <li>organization: 公司名称</li>
 * <li>description: 来访说明</li>
 * <li>address: 地点</li>
 * <li>authMethod: 授权方式,mobile/admin{@link com.everhomes.aclink.DoorAuthMethodType}</li>
 * <li>goFloor: 所到楼层</li>
 * <li>authRuleType: 授权规则种类，0 时长，1 次数 {@link com.everhomes.rest.aclink.DoorAuthRuleType}</li>
 * <li>totalAuthAmount: 授权的总开门次数</li>
 * <li>validAuthAmount: 剩余有效开门次数</li>
 * </ul>
 * @author janson
 *
 */
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
    private Byte authRuleType;
    private Integer totalAuthAmount;
    private Integer validAuthAmount;
    private String localAuthKey;
    private String qrString;
    private Byte rightOpen;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ItemType(FaceRecognitionPhotoDTO.class)
    FaceRecognitionPhotoDTO face;

    public FaceRecognitionPhotoDTO getFace() {
        return face;
    }

    public void setFace(FaceRecognitionPhotoDTO face) {
        this.face = face;
    }

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

    public Byte getAuthRuleType() {
		return authRuleType;
	}


	public void setAuthRuleType(Byte authRuleType) {
		this.authRuleType = authRuleType;
	}


	public Integer getTotalAuthAmount() {
		return totalAuthAmount;
	}


	public void setTotalAuthAmount(Integer totalAuthAmount) {
		this.totalAuthAmount = totalAuthAmount;
	}


	public Integer getValidAuthAmount() {
		return validAuthAmount;
	}


	public void setValidAuthAmount(Integer validAuthAmount) {
		this.validAuthAmount = validAuthAmount;
	}


	public String getLocalAuthKey() {
		return localAuthKey;
	}


	public void setLocalAuthKey(String localAuthKey) {
		this.localAuthKey = localAuthKey;
	}
	

	public String getQrString() {
		return qrString;
	}


	public void setQrString(String qrString) {
		this.qrString = qrString;
	}


	public Byte getRightOpen() {
		return rightOpen;
	}


	public void setRightOpen(Byte rightOpen) {
		this.rightOpen = rightOpen;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
