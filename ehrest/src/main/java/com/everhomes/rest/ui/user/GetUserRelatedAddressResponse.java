package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>familyList: 家庭列表，{@link com.everhomes.rest.family.FamilyDTO}</li>
 *  <li>organizationList: 机构列表，{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * </ul>
 */
public class GetUserRelatedAddressResponse {
    @ItemType(FamilyDTO.class)
    private List<FamilyDTO> familyList;
    
    @ItemType(OrganizationDetailDTO.class)
    private List<OrganizationDTO> organizationList;
    
	public List<FamilyDTO> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List<FamilyDTO> familyList) {
        this.familyList = familyList;
    }

    public List<OrganizationDTO> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<OrganizationDTO> organizationList) {
        this.organizationList = organizationList;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}