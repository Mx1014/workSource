package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.family.FamilyUserDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationUserDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>families: 家庭列表，参考{@link FamilyUserDTO}</li>
 *     <li>organizations: 公司列表，参考{@link OrganizationUserDTO}</li>
 * </ul>
 */
public class ListUserAddressResponse {
    private List<FamilyUserDTO> families;

    private List<OrganizationUserDTO> organizations;

    public List<FamilyUserDTO> getFamilies() {
        return families;
    }

    public void setFamilies(List<FamilyUserDTO> families) {
        this.families = families;
    }

    public List<OrganizationUserDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationUserDTO> organizations) {
        this.organizations = organizations;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}