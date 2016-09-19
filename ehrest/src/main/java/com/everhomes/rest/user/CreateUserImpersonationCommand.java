package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 创建马甲帐号
 * <li>ownerPhone: 马甲帐号</li>
 * <li>targetPhone: 替换的帐号</li>
 *  </ul>
 * @author janson
 *
 */
public class CreateUserImpersonationCommand {
    @NotNull
    private Integer namespaceId;
    @NotNull
    private String ownerPhone;
    @NotNull
    private String targetPhone;
    private String description;
    
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }
    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }
    public String getTargetPhone() {
        return targetPhone;
    }
    public void setTargetPhone(String targetPhone) {
        this.targetPhone = targetPhone;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
