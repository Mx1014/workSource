package com.everhomes.acl;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface ServiceModuleAppAuthorizationProvider {

    Long createServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj);

    Long createServiceModuleAppAuthorizations(List<ServiceModuleAppAuthorization> objs);

    void updateServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj);

    void updateServiceModuleAppAuthorizations(List<ServiceModuleAppAuthorization> objs);

    void deleteServiceModuleAppAuthorization(ServiceModuleAppAuthorization obj);

    ServiceModuleAppAuthorization getServiceModuleAppAuthorizationById(Long id);

    List<ServiceModuleAppAuthorization> queryServiceModuleAppAuthorizations(
            ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

}
