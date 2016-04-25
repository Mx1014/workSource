package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contactToken: 电话号码</li>
 * <li>addressId: 地址id</li>
 * </ul>
 */
public class DeleteOrganizationOwnerCommand {
	
	private Byte     contactType;
	private String   contactToken;
	private Long     addressId;

	
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


	public Long getAddressId() {
		return addressId;
	}


	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
