package com.everhomes.rest.techpark.company;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>id：主键id</li>
 * <li>ownerType：组别，参考{@link com.everhomes.techpark..punch.company.OwnerType}</li>
 * <li>ownerId：组id</li>
 * <li>contactUid：联系人id</li>
 * <li>contactType：联系类型，参考{@link com.everhomes.techpark..punch.company.ContactType}</li>
 * <li>contactToken：联系电话/邮箱</li>
 * <li>contactName：联系人名称</li>
 * <li>creatorUid：创建者Id</li>
 * <li>createTime：创建时间</li>
 * <li>updateUid：更新者id</li>
 * <li>updateTime:更新时间</li>
 * <li>deparment:部门</li>
 * <li>
 * </ul>
 */
public class GroupContactDTO {

	private java.lang.Long     id;
	private java.lang.String   ownerType;
	private java.lang.Long     ownerId;
	private java.lang.Long     contactUid;
	private java.lang.Byte     contactType;
	private java.lang.String   contactToken;
	private java.lang.String   contactName;
	private java.lang.Long     creatorUid;
	private java.lang.Long createTime;
	private java.lang.Long     updateUid;
	private java.lang.Long updateTime;
	private String department;
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(java.lang.String ownerType) {
		this.ownerType = ownerType;
	}
	public java.lang.Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(java.lang.Long ownerId) {
		this.ownerId = ownerId;
	}
	public java.lang.Long getContactUid() {
		return contactUid;
	}
	public void setContactUid(java.lang.Long contactUid) {
		this.contactUid = contactUid;
	}
	public java.lang.Byte getContactType() {
		return contactType;
	}
	public void setContactType(java.lang.Byte contactType) {
		this.contactType = contactType;
	}
	public java.lang.String getContactToken() {
		return contactToken;
	}
	public void setContactToken(java.lang.String contactToken) {
		this.contactToken = contactToken;
	}
	public java.lang.String getContactName() {
		return contactName;
	}
	public void setContactName(java.lang.String contactName) {
		this.contactName = contactName;
	}
	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public java.lang.Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.lang.Long createTime) {
		this.createTime = createTime;
	}
	public java.lang.Long getUpdateUid() {
		return updateUid;
	}
	public void setUpdateUid(java.lang.Long updateUid) {
		this.updateUid = updateUid;
	}
	public java.lang.Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.lang.Long updateTime) {
		this.updateTime = updateTime;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	
	
}
