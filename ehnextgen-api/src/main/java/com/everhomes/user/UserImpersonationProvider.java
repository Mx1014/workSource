package com.everhomes.user;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface UserImpersonationProvider {

    Long createUserImpersonation(UserImpersonation obj);

    void updateUserImpersonation(UserImpersonation obj);

    void deleteUserImpersonation(UserImpersonation obj);

    UserImpersonation getUserImpersonationById(Long id);

    List<UserImpersonation> queryUserImpersonations(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    UserImpersonation getUserImpersonationByOwnerId(Long userId);

}
