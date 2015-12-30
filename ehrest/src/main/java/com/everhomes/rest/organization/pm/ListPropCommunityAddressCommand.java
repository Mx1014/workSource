// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>addressId: 地址id</li>
 * </ul>
 */
public class ListPropCommunityAddressCommand {
	@NotNull
    private Long communityId;
    @NotNull
    private Long addressId;
   
    public ListPropCommunityAddressCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}



	public Long getAddressId() {
		return addressId;
	}


	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
