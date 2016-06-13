// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId：小区</li>
 * <li>addressId: 门牌号地址id</li>
 * </ul>
 */
public class CreatePropertyOrganizationCommand {
	
	@NotNull
	private Long communityId;
	
	private Long addressId;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
