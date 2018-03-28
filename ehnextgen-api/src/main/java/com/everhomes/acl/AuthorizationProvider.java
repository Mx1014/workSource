// @formatter:off
package com.everhomes.acl;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.menu.Target;
import com.everhomes.rest.module.Project;
import com.everhomes.util.Tuple;

import java.util.List;

public interface AuthorizationProvider {

    List<Authorization> listAuthorizations(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Boolean targetFlag);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Boolean targetFlag, CrossShardListingLocator locator, Integer pageSize);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType);

    List<Authorization> listAuthorizations(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, Long appId, String moduleControlType, Byte all_control_flag, Boolean targetFlag);

    Long createAuthorization(Authorization authorization);

    long createAuthorizations(List<Authorization> authorizations);

    void updateAuthorization(Authorization authorization);

    List<Authorization> deleteAuthorization(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType);

    void deleteAuthorizationById(Long id);

    void deleteAuthorizationWithConditon(Integer namespaceId, String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId, String identityType, String moduleControlType, Long appId, Long controlId);

    List<Long> getAuthorizationModuleIdsByTarget(List<Target> targets);

    //根据targets获取关联的应用管理员记录
    List<Tuple<Long,String>> getAuthorizationAppModuleIdsByTarget(List<Target> targets);

    //根据targets获取关联的应用管理员记录
    List<Tuple<Long,String>> getAuthorizationAppModuleIdsByTargetWithTypes(List<Target> targets, List<String> types);

    List<Tuple<Long, String>> getAuthorizationAppModuleIdsByTargetWithTypesAndConfigIds(List<Target> targets, List<String> types, List<Long> configIds);

    List<Authorization> listTargetAuthorizations(String ownerType, Long ownerId, String authType, Long authId, String identityType);

    List<Authorization> listManageAuthorizations(String ownerType, Long ownerId, String authType, Long authId);

    List<Authorization> listOrdinaryAuthorizations(String ownerType, Long ownerId, String authType, Long authId);

    List<Authorization> listManageAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId);

    List<Authorization> listOrdinaryAuthorizationsByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId);
    List<Authorization> listAuthorizationsByScope(String scope);
    List<AuthorizationRelation> listAuthorizationRelations(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    List<AuthorizationRelation> listAuthorizationRelations(CrossShardListingLocator locator, Integer pageSize, String ownerType, Long ownerId, Long moduleId, Long appId);

    List<AuthorizationRelation> listAuthorizationRelations(String ownerType, Long ownerId, Long moduleId);

    Long createAuthorizationRelation(AuthorizationRelation authorizationRelation);

    void deleteAuthorizationRelationById(Long id);

    void updateAuthorizationRelation(AuthorizationRelation authorizationRelation);

    AuthorizationRelation findAuthorizationRelationById(Long id);

    // 根据模块id和targets获取授权记录
    List<Project> getAuthorizationProjectsByAuthIdAndTargets(String authType, Long authId, List<Target> targets);

    // 根据模块id和targets获取授权记录
    List<Project> getAuthorizationProjectsByAuthIdAndTargets(String identityType, String authType, Long authId, List<Target> targets);

    // 根据应用id和targets获取授权记录
    List<Project> getAuthorizationProjectsByAppIdAndTargets(String identityType, String authType, Long authId, Long appId, List<Target> targets);

    List<String> getAuthorizationScopesByAuthAndTargets(String authType, Long authId, List<Target> targets);

    List<Project> getManageAuthorizationProjectsByAuthAndTargets(String authType, Long authId, List<Target> targets);

    Long getMaxControlIdInAuthorizations();

    // eh_authorization_control_configs表的接口
    Long createAuthorizationControlConfig(AuthorizationControlConfig authorizationControlConfig);

    // batch
    Long createAuthorizationControlConfigs(List<AuthorizationControlConfig> authorizationControlConfigs);

    void delteAuthorizationControlConfigsWithCondition(Integer namespaceId, Long userId);

    List listAuthorizationControlConfigs(Long userId, Long controlId);
}
