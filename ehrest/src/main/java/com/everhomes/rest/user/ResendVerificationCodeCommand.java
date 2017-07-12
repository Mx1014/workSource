// @formatter:off
package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;
/**
 * 重新发送验证码
 * @author elians
 * <ul>
 * <li>signupToken:注册令牌</li>
 * <li>namespaceId: 域ID/li>
 * <li>regionCode: 区域码</li>
 * </ul>
 */
public class ResendVerificationCodeCommand {
    @NotNull
    private String signupToken;
    
    private Integer namespaceId;

    @NotNull
    private Integer regionCode;
    
    public ResendVerificationCodeCommand() {
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getSignupToken() {
        return signupToken;
    }

    public void setSignupToken(String signupToken) {
        this.signupToken = signupToken;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    
}
