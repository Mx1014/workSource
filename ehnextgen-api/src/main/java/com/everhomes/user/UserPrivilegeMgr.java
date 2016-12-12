// @formatter:off
package com.everhomes.user;


public interface UserPrivilegeMgr {
    void checkUserPrivilege(long userId, long ownerId);

    /**
     * 新权限校验
     * @param ownerType
     * @param ownerId
     * @param organizationId
     * @param privilegeId
     * @return
     */
    boolean checkUserPrivilege(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId);


    /**
     * 新权限校验
     * @param ownerType
     * @param ownerId
     * @param organizationId
     * @param privilegeId
     */
    void checkUserAuthority(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId);

    /**
     * 校验公司管理员
     * @param organizationId
     * @return
     */
    boolean checkOrganizationAdmin(Long userId, Long organizationId);

    /**
     * 校验模块管理员权限
     * @param ownerType
     * @param ownerId
     * @param organizationId
     * @param privilegeId
     * @return
     */
    boolean checkModuleAdmin(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId);

    /**
     * 校验超级管理员
     * @param organizationId
     * @return
     */
    boolean checkSuperAdmin(Long userId, Long organizationId);

    /**
     * 校验角色权限
     * @param organizationId
     * @param privilegeId
     * @return
     */
    boolean checkRoleAccess(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId);

    /**
     * 校验权限是否被禁止
     * @param userId
     * @param ownerType
     * @param ownerId
     * @param privilegeId
     */
    void checkUserBlacklistAuthority(Long userId, String ownerType, Long ownerId, Long privilegeId);
}
