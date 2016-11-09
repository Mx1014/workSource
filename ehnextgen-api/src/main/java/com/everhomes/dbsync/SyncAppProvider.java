package com.everhomes.dbsync;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface SyncAppProvider {

    Long createSyncApp(SyncApp obj);

    void updateSyncApp(SyncApp obj);

    void deleteSyncApp(SyncApp obj);

    SyncApp getSyncAppById(Long id);

    List<SyncApp> querySyncApps(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

}
