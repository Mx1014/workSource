package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * 校验验证码
 *<ul>
 *<li>identifierToken:用户手机号或者邮箱</li>
 *<li>verifyCode:验证码</li>
 *</ul>
 */
public class CheckVerifyCodeCommand {
    @NotNull
    private String identifierToken;
    @NotNull
    private String verifyCode;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
