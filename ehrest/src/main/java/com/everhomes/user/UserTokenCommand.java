// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * 登录
 * @author elians
 *<ul>
 *<li>userIdentifier:用户标识，目前手机号和邮箱支持</li>
 *</ul>
 */
public class UserTokenCommand {
    @NotNull
    private String userIdentifier;
   
    
    public UserTokenCommand() {
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
