// @formatter:off
package com.everhomes.user;

import org.apache.commons.codec.binary.Base64;

import com.everhomes.rest.user.IdentifierType;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;

/**
 * 
 * TODO add signature to protect against tempering
 * 
 * SingupToken contains original login user identifier and the created user information, its token string
 * will be returned to the client and be used in user identifier claiming process
 * 
 * In the case of using mobile number for user identifier, the mobile number may be used for creating
 * multiple user accounts, but only one can be successfully claimed
 * 
 * @author Kelven Yang
 *
 */
public class SignupToken {
    private long userId;
    private IdentifierType identifierType;
    private String identifierToken;
    
    public SignupToken() {
    }

    public SignupToken(long userId, IdentifierType identifierType, String identifierToken) {
        this.userId = userId;
        this.identifierType = identifierType;
        this.identifierToken = identifierToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
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
