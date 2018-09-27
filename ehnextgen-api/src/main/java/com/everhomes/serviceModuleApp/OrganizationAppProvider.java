package com.everhomes.serviceModuleApp;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface OrganizationAppProvider {

    Long createOrganizationApp(OrganizationApp obj);

    void updateOrganizationApp(OrganizationApp obj);

    void deleteOrganizationApp(OrganizationApp obj);

    OrganizationApp getOrganizationAppById(Long id);

    List<OrganizationApp> queryOrganizationApps(ListingLocator locator,
            int count, ListingQueryBuilderCallback queryBuilderCallback);

    OrganizationApp findOrganizationAppsByOriginIdAndOrgId(Long appOriginId, Long orgId);
}
