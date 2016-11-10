package com.everhomes.dbsync;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface SyncMappingProvider {

    Long createSyncMapping(SyncMapping obj);

    void updateSyncMapping(SyncMapping obj);

    void deleteSyncMapping(SyncMapping obj);

    SyncMapping getSyncMappingById(Long id);

    List<SyncMapping> querySyncMappings(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

	SyncMapping findSyncMappingByName(String name);

	List<SyncMapping> getAllMappings(ListingLocator locator, int count);

}
