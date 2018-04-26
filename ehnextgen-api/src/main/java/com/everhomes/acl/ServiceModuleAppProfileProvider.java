package com.everhomes.acl;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ServiceModuleAppProfileProvider {

    Long createServiceModuleAppProfile(ServiceModuleAppProfile obj);

    void updateServiceModuleAppProfile(ServiceModuleAppProfile obj);

    void deleteServiceModuleAppProfile(ServiceModuleAppProfile obj);

    ServiceModuleAppProfile getServiceModuleAppProfileById(Long id);

    ServiceModuleAppProfile findServiceModuleAppProfileByOriginId(Long originId);

    List<ServiceModuleAppProfile> queryServiceModuleAppProfiles(
            ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

}
