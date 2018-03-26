package com.everhomes.serviceModuleApp;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AppCommunityConfigProvider {

    Long createAppCommunityConfig(AppCommunityConfig obj);

    void updateAppCommunityConfig(AppCommunityConfig obj);

    void deleteAppCommunityConfig(AppCommunityConfig obj);

    AppCommunityConfig getAppCommunityConfigById(Long id);

    List<AppCommunityConfig> queryAppCommunityConfigs(ListingLocator locator,
            int count, ListingQueryBuilderCallback queryBuilderCallback);

}
