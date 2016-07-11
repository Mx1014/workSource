// @formatter:off
package com.everhomes.user;


public interface UserPrivilegeMgr {
    void checkUserPrivilege(long userId, long ownerId);
}
