package com.everhomes.user;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface UserGroupHistoryProvider {

    Long createUserGroupHistory(UserGroupHistory history);

    void deleteUserGroupHistory(UserGroupHistory history);

    UserGroupHistory getHistoryById(Long id);

    List<UserGroupHistory> queryUserGroupHistoryByUserId(ListingLocator locator, Long userId, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    UserGroupHistory queryUserGroupHistoryByFamilyId(Long userId, Long familyId);

    List<UserGroupHistory> queryUserGroupHistoryByUserId(Long userId);

    UserGroupHistory queryUserGroupHistoryByAddressId(Long userId, Long addressId);

	List<UserGroupHistory> queryUserGroupHistoryByGroupIds(String userInfoKeyword, String communityKeyword, List<Long> communityIds, CrossShardListingLocator locator, int pageSize);

}
