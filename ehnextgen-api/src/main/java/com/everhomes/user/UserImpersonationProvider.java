package com.everhomes.user;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.user.UserImperInfo;

public interface UserImpersonationProvider {

    Long createUserImpersonation(UserImpersonation obj);

    void updateUserImpersonation(UserImpersonation obj);

    void deleteUserImpersonation(UserImpersonation obj);

    UserImpersonation getUserImpersonationById(Long id);

    List<UserImpersonation> queryUserImpersonations(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    UserImpersonation getUserImpersonationByOwnerId(Long userId);

    UserImpersonation getUserImpersonationByTargetId(Long userId);

    List<UserImpersonation> findOwnerUserImpersonations(Long userId, ListingLocator locator, int count);

    List<UserImpersonation> findTargetUserImpersonations(Long userId, ListingLocator locator, int count);

    List<UserImperInfo> searchUserByPhone(Integer namespaceId, String keyword, Byte impOnly,
            CrossShardListingLocator locator, int pageSize);

}
