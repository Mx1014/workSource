package com.everhomes.user;

import javax.validation.constraints.NotNull;
/**
 * 通过手机或者邮箱重新发送验证码
 * @author elians
 *identifier:用户手机号
 */
public class ResendVerificationCodeByIdentifierCommand {
    @NotNull
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
