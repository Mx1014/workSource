package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>videoToken: 用户授权信息，来自于 YZB </li>
 * </ul>
 * @author janson
 *
 */
public class UserVideoPermissionDTO {
    private String videoToken;
    private String sessionId;

    public String getVideoToken() {
        return videoToken;
    }

    public void setVideoToken(String videoToken) {
        this.videoToken = videoToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
