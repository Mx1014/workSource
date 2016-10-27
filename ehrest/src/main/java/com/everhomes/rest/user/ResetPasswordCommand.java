package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * 通过手机号设置密码
 * @author sfyan
 *<ul>
 *<li>identifierToken:用户手机号或者邮箱</li>
 *<li>newPassword:新密码</li>
 *</ul>
 */
public class ResetPasswordCommand {
    @NotNull
    private String identifierToken;
    @NotNull
    private String newPassword;

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
