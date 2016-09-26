package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>phone: 可以为空，默认用当前用户的手机号。</li>
 * <li>manufacturerType: YZB </li>
 * <li>videoToken: 用户ID</li>
 * </ul>
 * @author janson
 *
 */
public class RequestVideoPermissionCommand {
    private String manufacturerType;
    private String phone;
    private String videoToken; 
    private String sessionId;
    
    public String getManufacturerType() {
        return manufacturerType;
    }
    public void setManufacturerType(String manufacturerType) {
        this.manufacturerType = manufacturerType;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
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
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
