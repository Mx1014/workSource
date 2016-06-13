package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 设置账户信息
 * @author elians
 *<ul>
 *<li>accountName:帐号名</li>
 *<li>password:密码</li>
 *</ul>
 */
public class SetUserAccountInfoCommand {
    
    @NotNull
    String accountName;
    
    @NotNull
    String password;

    public SetUserAccountInfoCommand() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
