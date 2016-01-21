package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>用户所参与的实体列表
 * 	<li>familyList: 家庭列表</li>
 * 	<li>organizationList: 机构/公司列表</li>
 * </ul>
 * 
 */
public class ListUserRelatedEntitiesCommandResponse {
    @ItemType(FamilyDTO.class)
	private List<FamilyDTO> familyList;
	
    @ItemType(OrganizationSimpleDTO.class)
	private List<OrganizationSimpleDTO> organizationList;
    
	public List<FamilyDTO> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List<FamilyDTO> familyList) {
        this.familyList = familyList;
    }

    public List<OrganizationSimpleDTO> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<OrganizationSimpleDTO> organizationList) {
        this.organizationList = organizationList;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
