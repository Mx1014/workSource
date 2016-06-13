// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>familyId: 家庭id</li>
 * </ul>
 */
public class ListPropFamilyMemberCommand {
	@NotNull
    private Long communityId;
    @NotNull
    private Long familyId;
   
    public ListPropFamilyMemberCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	
	public Long getFamilyId() {
		return familyId;
	}


	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
