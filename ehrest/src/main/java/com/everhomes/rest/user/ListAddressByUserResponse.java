package com.everhomes.rest.user;

import com.everhomes.rest.family.FamilyUserDTO;
import com.everhomes.rest.organization.OrganizationUserDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>organizations: organizations 参考{@link OrganizationUserDTO}</li>
 *     <li>families: families 参考{@link FamilyUserDTO}</li>
 * </ul>
 */
public class ListAddressByUserResponse {

    private List<OrganizationUserDTO> organizations;
    private List<FamilyUserDTO> families;

    public List<OrganizationUserDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationUserDTO> organizations) {
        this.organizations = organizations;
    }

    public List<FamilyUserDTO> getFamilies() {
        return families;
    }

    public void setFamilies(List<FamilyUserDTO> families) {
        this.families = families;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}