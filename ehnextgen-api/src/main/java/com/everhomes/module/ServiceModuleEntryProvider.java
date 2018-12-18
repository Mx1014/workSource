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


    List<ServiceModuleAppEntry> listServiceModuleAppEntries(Long appId, Long appCategoryId, Byte terminalType, Byte locationType, Byte sceneType);

    List<ServiceModuleAppEntry> listServiceModuleAppEntries(List<Long> appIds, Byte locationType, Byte sceneType);

    void deleteAppEntry(Long id);

    void createAppEntry(ServiceModuleAppEntry serviceModuleAppEntry);

    ServiceModuleAppEntry findAppEntryById(Long id);

    void udpateAppEntry(ServiceModuleAppEntry serviceModuleAppEntry);

    void batchCreateAppEntry(List<ServiceModuleAppEntry> serviceModuleAppEntries);

    void batchDeleteAppEntry(List<ServiceModuleAppEntry> serviceModuleAppEntries);

    void batchUpdateAppEntry(List<ServiceModuleAppEntry> serviceModuleAppEntries);
}
