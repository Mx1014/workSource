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
    private Long authId;
    private String uid;

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
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