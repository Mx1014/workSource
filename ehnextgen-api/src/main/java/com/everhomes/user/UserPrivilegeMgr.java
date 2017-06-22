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
     */
    void checkUserAuthority(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId);

    /**
     * 校验公司管理员
     * @param currentOrgId
     * @return
     */
    boolean checkOrganizationAdmin(Long userId, Long currentOrgId);

    /**
     * 校验模块管理员权限
     * @param userId
     * @param ownerType
     * @param ownerId
     * @param privilegeId
     * @return
     */
    boolean checkModuleAdmin(Long userId, String ownerType, Long ownerId, Long privilegeId);

    /**
     * 校验模块管理员权限
     * @param ownerType
     * @param ownerId
     * @param userId
     * @param moduleId
     * @return
     */
    boolean checkModuleAdmin(String ownerType, Long ownerId, Long userId, Long moduleId);

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

    /**
     * 校验是否有模块全部权限
     * @param ownerType
     * @param ownerId
     * @param userId
     * @param privilegeId
     * @return
     */
    boolean checkModuleAllPrivileges(String ownerType, Long ownerId, Long userId, Long privilegeId);

    /**
     * 校验模块权限
     * @param ownerType
     * @param ownerId
     * @param userId
     * @param moduleId
     * @param type 模块管理权限类型和模块全部权限类型
     * @return
     */
    boolean checkModuleAccess(String ownerType, Long ownerId, Long userId, Long moduleId, ServiceModulePrivilegeType type);

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

    /**
     * 校验当前用户的权限
     * @param ownerType
     * @param ownerId
     * @param privilegeId
     */
    void checkCurrentUserAuthority(String ownerType, Long ownerId, Long privilegeId);

    void checkCurrentUserAuthority(String ownerType, Long ownerId, Long currentOrgId, Long privilegeId);
}
