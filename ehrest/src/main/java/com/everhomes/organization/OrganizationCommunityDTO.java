// @formatter:off
package com.everhomes.organization;

import com.everhomes.address.CommunityDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organization：机构详情</li>
 * <li>community：小区详情</li>
 * </ul>
 */
public class OrganizationCommunityDTO {
	
	private OrganizationDTO organization;
	
	private CommunityDTO community;

	
	public OrganizationCommunityDTO() {
		
	}
	

	public OrganizationDTO getOrganization() {
		return organization;
	}


	public void setOrganization(OrganizationDTO organization) {
		this.organization = organization;
	}


	public CommunityDTO getCommunity() {
		return community;
	}


	public void setCommunity(CommunityDTO community) {
		this.community = community;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
