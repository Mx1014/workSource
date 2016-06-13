// @formatter:off
package com.everhomes.rest.organization.pm;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>communityId: 小区id</li>
 * <li>contactType: 联系类型</li>
 * <li>contactToken: 联系信息</li>
 * <li>contactName: 姓名</li>
 * <li>contactDescription: 描述</li>
 * <li>addressId: 地址id</li>
 * <li>address: 地址</li>
 * <li>creatorUid: 创建的用户id</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class PropOwnerDTO {
	private Long     id;
	private Long     communityId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	private String   contactDescription;
	private Long     addressId;
	private String   address;
	private Long     creatorUid;
	private Timestamp createTime;
    
    public PropOwnerDTO() {
    }
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Byte getContactType() {
		return contactType;
	}
	public void setContactType(Byte contactType) {
		this.contactType = contactType;
	}
	public String getContactToken() {
		return contactToken;
	}
	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}
	public String getContactDescription() {
		return contactDescription;
	}
	public void setContactDescription(String contactDescription) {
		this.contactDescription = contactDescription;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
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
