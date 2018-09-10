// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 授权ID</li>
 * <li>doorId:门禁id</li>
 * <li>doorName:门禁名称</li>
 * <li>objectName: 授权对象名称</li>
 * <li>objectId:授权对象id</li>
 * <li>objectType:授权对象类型 0用户 1组织 2公司门牌 3家庭门牌 {@link com.everhomes.rest.aclink.AclinkAuthObjectType}</li>
 * <li>phone: 用户手机号</li>
 * <li>rightOpen: 0 开门未授权， 1 授权</li>
 * <li>rightVisitor: 0 访客未授权， 1 授权</li>
 * <li>rightRemote: 0 远程访问未授权， 1 授权</li>
 * <li>creatorId:创建者id</li>
 * <li>creatorName:创建者名称</li>
 * <li>createTime:创建时间 毫秒数</li>
 * <li>authType:授权类型  0常规授权 1临时 2令令访客 3左邻访客 4华润访客{@link com.everhomes.rest.aclink.DoorAuthType}</li>
 * <li>status:是否有效 0已失效1未失效{@link com.everhomes.rest.aclink.DoorAuthStatus}</li>
 * </ul>
 */
public class AclinkAuthDTO {
	private Long id;
	private Long doorId;
	private String doorName;
	private String objectName;
	private Long objectId;
	private Byte objectType;
	private String phone;
	private Byte rightOpen;
	private Byte rightVisitor;
	private Byte rightRemote;
	private Long creatorId;
	private String creatorName;
	private Timestamp createTime;
	private Byte authType;
	private Byte status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public Byte getObjectType() {
		return objectType;
	}
	public void setObjectType(Byte objectType) {
		this.objectType = objectType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Byte getRightOpen() {
		return rightOpen;
	}
	public void setRightOpen(Byte rightOpen) {
		this.rightOpen = rightOpen;
	}
	public Byte getRightVisitor() {
		return rightVisitor;
	}
	public void setRightVisitor(Byte rightVisitor) {
		this.rightVisitor = rightVisitor;
	}
	public Byte getRightRemote() {
		return rightRemote;
	}
	public void setRightRemote(Byte rightRemote) {
		this.rightRemote = rightRemote;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Byte getAuthType() {
		return authType;
	}
	public void setAuthType(Byte authType) {
		this.authType = authType;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
