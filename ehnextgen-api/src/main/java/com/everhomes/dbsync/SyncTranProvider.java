package com.everhomes.dbsync;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface SyncTranProvider {

    Long createSyncTran(SyncTran obj);

    void updateSyncTran(SyncTran obj);

    void deleteSyncTran(SyncTran obj);

    SyncTran getSyncTranById(Long id);

    List<SyncTran> querySyncTrans(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

}
