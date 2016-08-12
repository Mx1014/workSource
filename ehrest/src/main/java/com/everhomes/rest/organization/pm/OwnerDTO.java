package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OwnerAddressDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>identifierToken: 手机号</li>
 * <li>ownerName: 业主姓名</li>
 * <li>ownerAddresses: 业主地址信息 参考{@link com.everhomes.rest.organization.OwnerAddressDTO}</li>
 * </ul>
 */
public class OwnerDTO {
	
	private String identifierToken;
	
	private String ownerName;
	
	@ItemType(OwnerAddressDTO.class)
	private List<OwnerAddressDTO> ownerAddresses;
	
	public String getIdentifierToken() {
		return identifierToken;
	}

	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public List<OwnerAddressDTO> getOwnerAddresses() {
		return ownerAddresses;
	}

	public void setOwnerAddresses(List<OwnerAddressDTO> ownerAddresses) {
		this.ownerAddresses = ownerAddresses;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
