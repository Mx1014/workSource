// @formatter:off
package com.everhomes.acl;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.menu.Target;
import com.everhomes.rest.module.Project;

import java.util.List;

public interface AuthorizationProvider {

    List<Authorization> listAuthorizations(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Boolean targetFlag);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType);

    Long createAuthorization(Authorization authorization);

    long createAuthorizations(List<Authorization> authorizations);

    void updateAuthorization(Authorization authorization);

    List<Authorization> deleteAuthorization(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType);

    void deleteAuthorizationById(Long id);

    List<Long> getAuthorizationModuleIdsByTarget(List<Target> targets);

    List<Authorization> listTargetAuthorizations(String ownerType, Long ownerId, String authType, Long authId, String identityType);

    List<Authorization> listManageAuthorizations(String ownerType, Long ownerId, String authType, Long authId);

    List<Authorization> listOrdinaryAuthorizations(String ownerType, Long ownerId, String authType, Long authId);

    List<Authorization> listManageAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId);

    List<Authorization> listOrdinaryAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId);
    List<Authorization> listAuthorizationsByScope(String scope);
    List<AuthorizationRelation> listAuthorizationRelations(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    List<AuthorizationRelation> listAuthorizationRelations(CrossShardListingLocator locator, Integer pageSize, String ownerType, Long ownerId, Long moduleId);

    List<AuthorizationRelation> listAuthorizationRelations(String ownerType, Long ownerId, Long moduleId);

    Long createAuthorizationRelation(AuthorizationRelation authorizationRelation);

    void deleteAuthorizationRelationById(Long id);

    void updateAuthorizationRelation(AuthorizationRelation authorizationRelation);

    AuthorizationRelation findAuthorizationRelationById(Long id);

    List<Project> getAuthorizationProjectsByAuthIdAndTargets(String authType, Long authId, List<Target> targets);

    List<Project> getAuthorizationProjectsByAuthIdAndTargets(String identityType, String authType, Long authId, List<Target> targets);

    List<String> getAuthorizationScopesByAuthAndTargets(String authType, Long authId, List<Target> targets);

    List<Project> getManageAuthorizationProjectsByAuthAndTargets(String authType, Long authId, List<Target> targets);
}
