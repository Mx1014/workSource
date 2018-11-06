package com.everhomes.launchpad;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface LaunchPadIndexProvider {

    Long createLaunchPadIndex(LaunchPadIndex obj);

    void updateLaunchPadIndex(LaunchPadIndex obj);

    void deleteLaunchPadIndex(LaunchPadIndex obj);

    LaunchPadIndex getLaunchPadIndexById(Long id);

    List<LaunchPadIndex> queryLaunchPadIndexs(ListingLocator locator,
            int count, ListingQueryBuilderCallback queryBuilderCallback);

}
