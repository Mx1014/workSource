package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AesServerKeyProvider {

    Long createAesServerKey(AesServerKey obj);

    void updateAesServerKey(AesServerKey obj);

    void deleteAesServerKey(AesServerKey obj);

    AesServerKey getAesServerKeyById(Long id);

    List<AesServerKey> queryAesServerKeyByDoorId(ListingLocator locator, Long refId, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<AesServerKey> queryAesServerKeys(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    AesServerKey queryAesServerKeyByDoorId(Long doorId, Long ver);

}
