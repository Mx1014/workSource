// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;
import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>ownerId: 门禁所属组织ID</li>
 * <li>ownerName:项目/企业名称</li>
 * <li>ownerType:门禁所属组织类型</li>
 * <li>status: 授权状态， 0 失效，1 有效</li>
 * <li>authType:授权类型，{@link com.everhomes.rest.aclink.DoorAuthType}</li>
 * <li>userId: 用户id</li>
 * <li>createTime: 创建时间</li>
 * <li>doorId: 门禁ID 正式授权不为空 临时授权时与groupId有且仅有一个有值</li>
 * <li>groupId: 门禁组ID 正式授权时为空</li>
 * <li>doorName:门禁名称</li>
 * <li>groupName:门禁组名称</li>
 * <li>hardwareId：门禁mac地址</li>
 * <li>authMethod: 授权方式,mobile/admin{@link com.everhomes.aclink.DoorAuthMethodType}</li>
 * </ul>
 */
public class DoorAuthLiteDTO implements Comparable<DoorAuthLiteDTO>, Serializable{
    private Long id;
    private Long ownerId;
    private String ownerName;
    private Byte ownerType;
    private Byte status;
    private Byte authType;
    private Long userId;
    private Timestamp createTime;
    private Long doorId;
    private String doorName;
    private Long groupId;
    private String groupName;
    private String hardwareId;
    private String authMethod;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Byte getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
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
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
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
	public String getAuthMethod() {
		return authMethod;
	}
	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	@Override
	public int compareTo(DoorAuthLiteDTO o) {
		if(this.createTime.getTime() < o.getCreateTime().getTime()){
			return 1;
		}
		return -1;
	}
}
