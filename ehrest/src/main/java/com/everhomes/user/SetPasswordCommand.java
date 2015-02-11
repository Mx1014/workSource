// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

public class SetPasswordCommand {
    private String oldPassword;
    
    @NotNull
    private String newPassword;

    public SetPasswordCommand() {
    }
    
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
