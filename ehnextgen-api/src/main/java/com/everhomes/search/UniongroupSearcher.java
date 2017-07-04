package com.everhomes.search;

import com.everhomes.organization.Organization;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.search.OrganizationQueryResult;

import java.util.List;

/**
 * Created by lei.lv on 2017/7/3.
 */
public interface UniongroupSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Organization> organizations);
    void feedDoc(Organization organization);
    void syncFromDb();
    GroupQueryResult query(SearchOrganizationCommand cmd);
}
