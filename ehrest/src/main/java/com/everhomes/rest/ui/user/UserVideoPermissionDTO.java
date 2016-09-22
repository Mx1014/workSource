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

    public String getVideoToken() {
        return videoToken;
    }

    public void setVideoToken(String videoToken) {
        this.videoToken = videoToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
