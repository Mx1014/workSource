// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class VerifyAndLogonCommand {
    @NotNull
    private String signupToken;
    
    @NotNull
    private String verificationCode;
    
    @NotNull
    private String initialPassword;
    
    private String deviceIdentifier;
    
    private Integer namespaceId;
    
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
