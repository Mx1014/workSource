package com.everhomes.smartcard;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface SmartCardKeyProvider {

    Long createSmartCardKey(SmartCardKey obj);

    void updateSmartCardKey(SmartCardKey obj);

    void deleteSmartCardKey(SmartCardKey obj);

    SmartCardKey getSmartCardKeyById(Long id);

    List<SmartCardKey> querySmartCardKeys(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<SmartCardKey> queryLatestSmartCardKeys(Long userId);

}
