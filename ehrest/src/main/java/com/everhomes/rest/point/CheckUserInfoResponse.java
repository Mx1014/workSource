package com.everhomes.rest.point;

import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>userInfo: userInfo {@link com.everhomes.rest.user.UserInfo}</li>
 *     <li>captchaImg: captchaImg</li>
 * </ul>
 */
public class CheckUserInfoResponse {

    private UserInfo userInfo;
    private String captchaImg;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getCaptchaImg() {
        return captchaImg;
    }

    public void setCaptchaImg(String captchaImg) {
        this.captchaImg = captchaImg;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
