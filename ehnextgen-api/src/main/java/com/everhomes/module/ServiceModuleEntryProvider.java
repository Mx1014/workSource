// @formatter:off
package com.everhomes.module;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import org.jooq.Condition;

import java.util.List;
import java.util.Map;

public interface ServiceModuleEntryProvider {
    List<ServiceModuleEntry> listServiceModuleEntries(Long moduleId, Long appCategoryId, Byte terminalType, Byte locationType, Byte sceneType);

    List<ServiceModuleEntry> listServiceModuleEntries(List<Long> moduleIds, Byte locationType, Byte sceneType);

    void delete(Long id);

    void create(ServiceModuleEntry serviceModuleEntry);

    ServiceModuleEntry findById(Long id);

    void udpate(ServiceModuleEntry serviceModuleEntry);
}
