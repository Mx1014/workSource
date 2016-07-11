package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AesUserKeyProvider {

    Long createAesUserKey(AesUserKey obj);

    void updateAesUserKey(AesUserKey obj);

    void deleteAesUserKey(AesUserKey obj);

    AesUserKey getAesUserKeyById(Long id);

    List<AesUserKey> queryAesUserKeyByDoorId(ListingLocator locator, Long refId, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<AesUserKey> queryAesUserKeys(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    AesUserKey queryAesUserKeyByDoorId(Long doorId, Long userId);

    Long prepareForAesUserKeyId();

    AesUserKey queryAesUserKeyByDoorId(Long doorId, Long userId, Long ignoreAuthId);

    AesUserKey queryAesUserKeyByAuthId(Long doorId, Long authId);

    List<AesUserKey> queryAdminAesUserKeyByUserId(Long userId, int maxCount);

}
