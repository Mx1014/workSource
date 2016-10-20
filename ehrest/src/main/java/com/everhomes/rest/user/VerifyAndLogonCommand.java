// @formatter:off
package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>signupToken:注册临时令牌</li>
 *<li>verificationCode:验证码</li>
 *<li>initialPassword:初始化密码</li>
 *<li>deviceIdentifier:设备标识</li>
 *<li>invitationCode:邀请码</li>
 *<li>namespaceId:名字空间</li>
 *<li>nickName:昵称</li>
 * <li>regionCode:区号</li>
 *</ul>
 */
public class VerifyAndLogonCommand {
    @NotNull
    private String signupToken;
    
    @NotNull
    private String verificationCode;
    
    @NotNull
    private String initialPassword;
    
    private String nickName;
    
    private String invitationCode;
    
    private String deviceIdentifier;
    
    private Integer namespaceId;
    
    private String pusherIdentify;

    private Integer regionCode;
    
    public VerifyAndLogonCommand() {
    }

    public String getSignupToken() {
        return signupToken;
    }

    public void setSignupToken(String signupToken) {
        this.signupToken = signupToken;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
    
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }
    

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }
    

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
