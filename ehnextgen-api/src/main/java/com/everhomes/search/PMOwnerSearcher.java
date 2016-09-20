package com.everhomes.search;

import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.organization.pm.OrganizationOwnerCar;
import com.everhomes.rest.organization.pm.ListOrganizationOwnersResponse;
import com.everhomes.rest.organization.pm.SearchOrganizationOwnersCommand;

import java.util.List;

public interface PMOwnerSearcher {
	void deleteById(Long id);
    void bulkUpdate(List<CommunityPmOwner> owners);
    void feedDoc(CommunityPmOwner owner);
    void syncFromDb();
    ListOrganizationOwnersResponse query(SearchOrganizationOwnersCommand cmd);
}
