// @formatter:off
package com.everhomes.rest.user;

import org.apache.commons.codec.binary.Base64;

import com.everhomes.util.StringHelper;
import com.google.gson.Gson;

/**
 * 
 * TODO add signature to protect against tempering
 * 
 * Represents token object for client login
 * property loginId is unique only on per-user basis (not a global unique identifier)
 * 
 * @author Kelven Yang
 *
 */
public class LoginToken {
    private long userId;
    private int loginId;
    private int loginInstanceNumber;
    
    public LoginToken() {
    }

    public LoginToken(long userId, int loginId, int instanceNumber) {
        this.userId = userId;
        this.loginId = loginId;
        this.loginInstanceNumber = instanceNumber;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public int getLoginInstanceNumber() {
        return loginInstanceNumber;
    }

    public void setLoginInstanceNumber(int loginInstanceNumber) {
        this.loginInstanceNumber = loginInstanceNumber;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
