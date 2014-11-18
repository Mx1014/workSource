package com.everhomes.user;

import javax.validation.constraints.NotNull;

public class VerifyAndLogonCommand {
    @NotNull
    private String signupToken;
    
    @NotNull
    private String verificationCode;
    
    private String deviceIdentifier;
    
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
}
