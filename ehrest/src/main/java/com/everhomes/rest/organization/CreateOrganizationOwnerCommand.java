package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>contactName: 用户名</li>
 * <li>contactToken: 电话号码</li>
 * <li>contactDescription: 描述</li>
 * <li>addressId: 地址id</li>
 * <li>communityId: 小区id</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>apartmentName: 公寓名称</li>
 * </ul>
 */
public class CreateOrganizationOwnerCommand {
	
	private Long     id;
	private Long     organizationId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	private String   contactDescription;
	private Long     addressId;
	
	private Long communityId;
	
	private String buildingName;
	
	private String apartmentName;

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



	public Long getCommunityId() {
		return communityId;
	}



	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}



	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getApartmentName() {
		return apartmentName;
	}



	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
