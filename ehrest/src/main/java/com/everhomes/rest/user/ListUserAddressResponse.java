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
 *     <li>familyList: 家庭列表，{@link FamilyDTO}</li>
 *     <li>organizationList: 机构列表，{@link OrganizationDTO}</li>
 * </ul>
 */
public class ListUserAddressResponse {
    @ItemType(FamilyUserDTO.class)
    private List<FamilyUserDTO> familyList;

    @ItemType(OrganizationDetailDTO.class)
    private List<OrganizationUserDTO> organizationList;

    public List<FamilyUserDTO> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List<FamilyUserDTO> familyList) {
        this.familyList = familyList;
    }

    public List<OrganizationUserDTO> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<OrganizationUserDTO> organizationList) {
        this.organizationList = organizationList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}