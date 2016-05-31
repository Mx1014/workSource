package com.everhomes.rest.wifi;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: wifi ID</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.wifi.WifiOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>ssid: 网络名称</li>
 * <li>creatorUid: 创建人ID</li>
 * <li>deleteUid: 删除人I</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class WifiSettingDTO {
	private java.lang.Long     id;
	private java.lang.String   ownerType;
	private java.lang.Long     ownerId;
	private java.lang.String   ssid;
	private java.lang.Long     creatorUid;
	private java.lang.Long     deleteUid;
	private java.sql.Timestamp createTime;
	
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
	public java.lang.String getSsid() {
		return ssid;
	}
	public void setSsid(java.lang.String ssid) {
		this.ssid = ssid;
	}
	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public java.lang.Long getDeleteUid() {
		return deleteUid;
	}
	public void setDeleteUid(java.lang.Long deleteUid) {
		this.deleteUid = deleteUid;
	}
	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
