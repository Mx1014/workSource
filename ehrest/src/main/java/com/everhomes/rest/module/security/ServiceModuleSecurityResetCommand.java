package com.everhomes.rest.module.security;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: ownerType对应的对象ID</li>
 * <li>ownerType: 对象类型</li>
 * <li>moduleId: 应用ID</li>
 * <li>verifyCode:验证码</li>
 * <li>newPassword:新密码</li>
 * </ul>
 */
public class ServiceModuleSecurityResetCommand {
    private Long ownerId;
    private String ownerType;
    private Long moduleId;
    private String verifyCode;
    private String newPassword;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
