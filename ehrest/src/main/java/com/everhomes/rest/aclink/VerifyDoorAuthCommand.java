// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>authId: 左邻授权Id</li>
 * <li>uid[String]: 鼎芯锁uid ，等于对应门禁uuid</li>
 * </ul>
 *
 */
public class VerifyDoorAuthCommand {
    private Long userId;
    private String authType;
    private String uid;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}