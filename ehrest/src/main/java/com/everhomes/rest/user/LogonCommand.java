// @formatter:off
package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * 登录
 * @author elians
 *<ul>
 *<li>userIdentifier:用户标识，目前手机号和邮箱支持</li>
 *<li>password:密码</li>
 *<li>deviceIdentifier:设备标识</li>
 *<li>namespaceId:名字空间，目前默认为0</li>
 *<li>partnerId: 园区合作商</li>
 *<li>regionCode: 国家区号</li>
 *</ul>
 */
public class LogonCommand {
    @NotNull
    private String userIdentifier;
    
    @NotNull
    private String password;
    
    private String deviceIdentifier;
    
    private Integer namespaceId;
    
    private String pusherIdentify;

    private Integer regionCode;
    
    public LogonCommand() {
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
    
    
    public Integer getNamespaceId() {
        return this.namespaceId;
    }
    
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getPusherIdentify() {
        return pusherIdentify;
    }

    public void setPusherIdentify(String pusherIdentify) {
        this.pusherIdentify = pusherIdentify;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
