package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 通过用户标识设置密码
 * @author elians
 *<ul>
 *<li>identifierToken:用户手机号或者邮箱</li>
 *<li>verifyCode:验证码</li>
 *<li>newPassword:新密码</li>
 *</ul>
 */
public class VerifyAndResetPasswordCommand {
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
