package com.everhomes.rest.organization;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 *
 */
public class OrganizationOwnerDTO {
	
	private Long     id;
	private Long     organizationId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	private String   contactDescription;
	private Long     addressId;
	private String   address;
	private Long     creatorUid;
	private Timestamp createTime;
	
	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
