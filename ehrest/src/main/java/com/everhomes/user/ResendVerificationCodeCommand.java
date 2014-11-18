package com.everhomes.user;

import javax.validation.constraints.NotNull;

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
