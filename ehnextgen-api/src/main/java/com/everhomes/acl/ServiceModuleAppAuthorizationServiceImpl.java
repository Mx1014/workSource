package com.everhomes.acl;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.server.schema.Tables;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceModuleAppAuthorizationServiceImpl implements ServiceModuleAppAuthorizationService{

    static final Integer MAX_COUNT_IN_A_QUERY = 1000000;

    @Autowired
    private ServiceModuleAppAuthorizationProvider serviceModuleAppAuthorizationProvider;

    @Override
    public boolean checkCommunityRelationOfOrgId(Integer namespaceId, Long currentOrgId, Long checkCommunityId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(currentOrgId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.PROJECT_ID.eq(checkCommunityId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                return query;
            }
        });
        return authorizations.size() > 0;
    }

    @Override
    public List<ProjectDTO> listCommunityRelationOfOrgId(Integer namespaceId, Long organizationId) {
        return null;
    }

}
