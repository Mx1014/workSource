// @formatter:off
package com.everhomes.user;


import com.everhomes.acl.AclRoleDescriptor;
import com.everhomes.module.ServiceModulePrivilegeType;

import java.util.List;

public interface UserPrivilegeMgr {
    void checkUserPrivilege(long userId, long ownerId);

    /**
     * 新权限校验
     * @param ownerType
     * @param ownerId
     * @param currentOrgId
     * @param privilegeId
     * @return
     */
    boolean checkUserPrivilege(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId);

    /**
     * 新权限校验
     * @param ownerType
     * @param ownerId
     * @param currentOrgId
     * @param privilegeId
     * @param appId
     * @param checkOrgId
     *@param checkCommunityId @return
     */
    boolean checkUserPrivilege(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId, Long appId, Long checkOrgId, Long checkCommunityId);


    /**
     * 新权限校验
     * @param ownerType
     * @param ownerId
     * @param currentOrgId
     * @param privilegeId
     */
    void checkUserAuthority(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId);

//    /**
//     * 校验公司管理员
//     * @param currentOrgId
//     * @return
//     */
//    boolean checkOrganizationAdmin(Long userId, Long currentOrgId);

//    /**
//     * 校验模块管理员权限
//     * @param userId
//     * @param ownerType
//     * @param ownerId
//     * @param privilegeId
//     * @return
//     */
//    boolean checkModuleAdmin(Long userId, String ownerType, Long ownerId, Long privilegeId);
//
//    /**
//     * 校验模块管理员权限
//     * @param ownerType
//     * @param ownerId
//     * @param userId
//     * @param moduleId
//     * @return
//     */
//    boolean checkModuleAdmin(String ownerType, Long ownerId, Long userId, Long moduleId);

    /**
     * 校验超级管理员
     * @param currentOrgId
     * @return
     */
    boolean checkSuperAdmin(Long userId, Long currentOrgId);

    /**
     * 校验角色权限
     * @param currentOrgId
     * @param privilegeId
     * @return
     */
    boolean checkRoleAccess(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId);

    /**
     * 校验权限是否被禁止
     * @param userId
     * @param ownerType
     * @param ownerId
     * @param privilegeId
     */
    void checkUserBlacklistAuthority(Long userId, String ownerType, Long ownerId, Long privilegeId);

//    /**
//     * 校验是否有模块全部权限
//     * @param ownerType
//     * @param ownerId
//     * @param userId
//     * @param privilegeId
//     * @return
//     */
//    boolean checkModuleAllPrivileges(String ownerType, Long ownerId, Long userId, Long privilegeId);

//    /**
//     * 校验模块权限
//     * @param ownerType
//     * @param ownerId
//     * @param userId
//     * @param moduleId
//     * @param type 模块管理权限类型和模块全部权限类型
//     * @return
//     */
//    boolean checkModuleAccess(String ownerType, Long ownerId, Long userId, Long moduleId, ServiceModulePrivilegeType type);

    /**
     * 校验模块权限
     * @param ownerType
     * @param ownerId
     * @param descriptors
     * @param moduleId
     * @param type
     * @return
     */
    boolean checkModuleAccess(String ownerType, Long ownerId, List<AclRoleDescriptor> descriptors, Long moduleId, ServiceModulePrivilegeType type);

    /**
     * 校验模块权限
     * @param ownerType
     * @param ownerId
     * @param descriptors
     * @param privilegeId
     * @return
     */
    boolean checkModuleAllPrivileges(String ownerType, Long ownerId, List<AclRoleDescriptor> descriptors, Long privilegeId);

//    /**
//     * 校验当前用户在项目范围内的权限
//     * @param ownerType
//     * @param ownerId
//     * @param privilegeId
//     */
//    void checkCurrentUserAuthority(String ownerType, Long ownerId, Long privilegeId);

//    /**
//     * 校验当前用户在某一个项目范围以及当前公司内的权限
//     * @param ownerType
//     * @param ownerId
//     * @param currentOrgId
//     * @param privilegeId
//     */
//    void checkCurrentUserAuthority(String ownerType, Long ownerId, Long currentOrgId, Long privilegeId);

    /**
     * 校验用户在部分范围内以及当前公司内的权限
     * @param currentOrgId
     * @param privilegeId
     */
    void checkCurrentUserAuthority(Long currentOrgId, Long privilegeId);

//    /**
//     * 全部模块管理员
//     * @param ownerType
//     * @param ownerId
//     * @param userId
//     * @return
//     */
//    boolean checkAllModuleAdmin(String ownerType, Long ownerId, Long userId);

    /**
     * 检查应用管理员
     * @param ownerType
     * @param ownerId
     * @param userId
     * @param privilegeId
     * @param appId
     * @param communityId
     * @param organizationId
     * @return
     */
    boolean checkModuleAppAdmin(String ownerType, Long ownerId, Long userId, Long privilegeId, Long appId, Long communityId, Long organizationId);

    /**
     * 检查应用管理员
     * @param ownerType
     * @param ownerId
     * @param userId
     * @param moduleId
     * @param appId  @return
     * @param communityId
     * @param organizationId
     */
    boolean checkModuleAppAdmin(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long moduleId, Long appId, Long communityId, Long organizationId);


    /**
     * 简单检查用户是否对某个应用是管理员
     * @param namespaceId
     * @param userId
     * @param appId
     * @return
     */
    boolean checkModuleAppAdmin(Integer namespaceId, Long organizationId, Long userId, Long appId);


//    /**
//     * 校验权限细化
//     * @param namespaceId
//     * @param communityId
//     * @param userId
//     * @param appId
//     * @return
//     */
//    boolean checkModuleAppRelation(Integer namespaceId, Long communityId, Long userId, Long appId);

    /**
     * 校验用户权限
     * @param userId
     * @param currentOrgId
     * @param privilegeId
     * @param moduleId
     * @param actionType
     *@param customTag
     * @param checkOrgId
     * @param checkCommunityId    @return
     */
    boolean checkUserPrivilege(Long userId, Long currentOrgId, Long privilegeId, Long moduleId, Byte actionType, String customTag, Long checkOrgId, Long checkCommunityId);

    /**
     * 新运营后台-校验用户权限
     * @param userId
     * @param currentOrgId
     * @param privilegeId
     * @param appId
     * @param checkOrgId
     * @param checkCommunityId    @return
     */
    boolean checkUserPrivilege(Long userId, Long currentOrgId, Long privilegeId, Long appId, Long checkOrgId, Long checkCommunityId);

}
