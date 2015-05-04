// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;
/**
 * 重新发送验证码
 * @author elians
 *signupToken:注册令牌
 */
public class ResendVerificationCodeCommand {
    @NotNull
    private String signupToken;
    
    public ResendVerificationCodeCommand() {
    }

    public String getSignupToken() {
        return signupToken;
    }

    public void setSignupToken(String signupToken) {
        this.signupToken = signupToken;
    }
}
