package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.organization.OrgAddressDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>familyList: 家庭列表，{@link com.everhomes.rest.family.FamilyDTO}</li>
 *  <li>organizationList: 机构列表，{@link com.everhomes.rest.organization.OrgAddressDTO}</li>
 * </ul>
 */

public class GetUserRelatedAddressByCommunityResponse {
    private String userName;
    @ItemType(FamilyDTO.class)
    private List<FamilyDTO> familyList;
    @ItemType(PmTaskHistoryAddressDTO.class)
    private List<PmTaskHistoryAddressDTO> historyAddresses;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<OrgAddressDTO> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<OrgAddressDTO> organizationList) {
		this.organizationList = organizationList;
	}


	@ItemType(OrgAddressDTO.class)
    private List<OrgAddressDTO> organizationList;

    public List<FamilyDTO> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List<FamilyDTO> familyList) {
        this.familyList = familyList;
    }

    public List<PmTaskHistoryAddressDTO> getHistoryAddresses() {
        return historyAddresses;
    }

    public void setHistoryAddresses(List<PmTaskHistoryAddressDTO> historyAddresses) {
        this.historyAddresses = historyAddresses;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
