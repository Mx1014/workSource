// @formatter:off
package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;
/**
 * 设置密码
 * @author elians
 *<ul>
 *<li>oldPassword:旧密码</li>
 *<li>newPassword:新密码</li>
 *</ul>
 */
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
