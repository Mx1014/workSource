package com.everhomes.oauthapi;

import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>members: members</li>
 * </ul>
 */
public class UserAuthenticationInfoDTO {

    private List<OrganizationMemberDTO> members;

    public List<OrganizationMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<OrganizationMemberDTO> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
