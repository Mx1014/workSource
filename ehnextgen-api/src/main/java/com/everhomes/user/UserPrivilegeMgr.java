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
     */
    void checkUserAuthority(String ownerType, Long ownerId, Long organizationId, Long privilegeId);
}
