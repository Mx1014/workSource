// @formatter:off
package com.everhomes.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.address.CommunityDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organization：机构详情</li>
 * <li>communityList：对应小区详情</li>
 * </ul>
 */
public class OrganizationCommunityDTO {
	
	private OrganizationDTO organization;
	
	@ItemType(Long.class)
	private List<CommunityDTO> communityList;

	
	public OrganizationCommunityDTO() {
		
	}
	

	public OrganizationDTO getOrganization() {
		return organization;
	}


	public void setOrganization(OrganizationDTO organization) {
		this.organization = organization;
	}


	public List<CommunityDTO> getCommunityList() {
		return communityList;
	}


	public void setCommunityList(List<CommunityDTO> communityList) {
		this.communityList = communityList;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
