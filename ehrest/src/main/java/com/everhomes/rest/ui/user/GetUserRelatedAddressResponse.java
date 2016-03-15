package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;

/**
 * <ul>
 *  <li>familyList: 家庭列表</li>
 *  <li>organizationList: 机构列表</li>
 * </ul>
 */
public class GetUserRelatedAddressResponse {
    @ItemType(FamilyDTO.class)
    private List<FamilyDTO> familyList;
    
    @ItemType(OrganizationDetailDTO.class)
    private List<OrganizationDetailDTO> organizationList;
}
