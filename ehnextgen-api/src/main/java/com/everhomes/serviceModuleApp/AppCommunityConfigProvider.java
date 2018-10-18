package com.everhomes.serviceModuleApp;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AppCommunityConfigProvider {

    Long createAppCommunityConfig(AppCommunityConfig obj);

    void updateAppCommunityConfig(AppCommunityConfig obj);

    void deleteAppCommunityConfig(AppCommunityConfig obj);

    void deleteAppCommunityConfigByCommunityId(Long communityId);

    void deleteAppCommunityConfigByCommunityIdAndAppOriginId(Long communityId, Long AppOriginId);

    AppCommunityConfig getAppCommunityConfigById(Long id);

    AppCommunityConfig findAppCommunityConfigByCommunityIdAndAppOriginId(Long communityId, Long appOriginId);

    List<AppCommunityConfig> queryAppCommunityConfigs(ListingLocator locator,
                                                      int count, ListingQueryBuilderCallback queryBuilderCallback);

}
