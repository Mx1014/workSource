// @formatter:off
package com.everhomes.acl;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface AuthorizationProvider {

    List<Authorization> listAuthorizations(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Boolean targetFlag);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType);

    Long createAuthorization(Authorization authorization);

    List<Authorization> deleteAuthorization(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType);

    void deleteAuthorizationById(Long id);

    List<Authorization> listTargetAuthorizations(String ownerType, Long ownerId, String authType, Long authId, String identityType);

    List<Authorization> listManageAuthorizations(String ownerType, Long ownerId, String authType, Long authId);

    List<Authorization> listOrdinaryAuthorizations(String ownerType, Long ownerId, String authType, Long authId);

    List<Authorization> listManageAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId);

    List<Authorization> listOrdinaryAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId);
}
