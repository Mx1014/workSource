package com.everhomes.search;

import com.everhomes.organization.pm.OrganizationOwnerCar;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerCarResponse;
import com.everhomes.rest.organization.pm.SearchOrganizationOwnerCarCommand;

import java.util.List;

public interface OrganizationOwnerCarSearcher {

	void deleteById(Long id);

    void bulkUpdate(List<OrganizationOwnerCar> carList);

    void feedDoc(OrganizationOwnerCar owner);

    void syncFromDb();

    ListOrganizationOwnerCarResponse query(SearchOrganizationOwnerCarCommand cmd);
}
