package com.everhomes.search;

import java.util.List;

import com.everhomes.organization.Organization;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.search.OrganizationQueryResult;

public interface OrganizationSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Organization> organizations);
    void feedDoc(Organization organization);
    void syncFromDb();
    GroupQueryResult query(SearchOrganizationCommand cmd); 
    OrganizationQueryResult queryOrganization(SearchOrganizationCommand cmd);
}
