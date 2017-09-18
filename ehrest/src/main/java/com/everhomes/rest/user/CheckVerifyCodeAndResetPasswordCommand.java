package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>identifierToken: 用户手机号或者邮箱</li>
 *     <li>verifyCode: 验证码</li>
 *     <li>newPassword: 新密码</li>
 * </ul>
 */
public class CheckVerifyCodeAndResetPasswordCommand {
    @NotNull
    private String identifierToken;
    @NotNull
    private String verifyCode;
    @NotNull
    private String newPassword;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
