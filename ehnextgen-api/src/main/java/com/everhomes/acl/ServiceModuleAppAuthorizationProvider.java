package com.everhomes.acl;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ServiceModuleAppAuthorizationProvider {

    Long createServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj);

    void updateServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj);

    void deleteServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj);

    ServiceModuleAppAuthorization getServiceModuleAppAuthorizationById(Long id);

    List<ServiceModuleAppAuthorization> queryServiceModuleAppAuthorizations(
            ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

}
