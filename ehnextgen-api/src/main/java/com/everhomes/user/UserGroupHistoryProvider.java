package com.everhomes.user;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface UserGroupHistoryProvider {

    Long createUserGroupHistory(UserGroupHistory history);

    void deleteUserGroupHistory(UserGroupHistory history);

    UserGroupHistory getHistoryById(Long id);

    List<UserGroupHistory> queryUserGroupHistoryByUserId(ListingLocator locator, Long userId, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    UserGroupHistory queryUserGroupHistoryByFamilyId(Long userId, Long familyId);

    List<UserGroupHistory> queryUserGroupHistoryByUserId(Long userId);

    UserGroupHistory queryUserGroupHistoryByAddressId(Long userId, Long addressId);

	List<UserGroupHistory> queryUserGroupHistoryByGroupIds(List<Long> groupIds, CrossShardListingLocator locator, int pageSize);

}
