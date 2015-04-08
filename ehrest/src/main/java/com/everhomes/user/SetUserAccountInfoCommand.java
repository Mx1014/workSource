package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

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
