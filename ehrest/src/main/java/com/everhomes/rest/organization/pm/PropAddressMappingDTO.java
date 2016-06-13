// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>communityId: 小区id</li>
 * <li>addressId: 地址id</li>
 * <li>addressName:地址名称 </li>
 * <li>organizationAddress:物业映射地址名称 </li>
 * </ul>
 */
public class PropAddressMappingDTO {
	private Long   id;
	private Long   communityId;
	private Long   addressId;
	private String addressName;
    private String organizationAddress;

    public PropAddressMappingDTO() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}



    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getAddressId() {
		return addressId;
	}


	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}



	public String getAddressName() {
		return addressName;
	}


	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}


	public String getOrganizationAddress() {
		return organizationAddress;
	}


	public void setOrganizationAddress(String organizationAddress) {
		this.organizationAddress = organizationAddress;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
