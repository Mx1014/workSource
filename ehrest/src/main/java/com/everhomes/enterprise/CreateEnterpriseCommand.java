package com.everhomes.enterprise;

/**
 * <ul>管理后台使用，应该使用批量添加实现</ul>
 * @author janson
 *
 */
public class CreateEnterpriseCommand {
    private java.lang.String   name;
    private java.lang.String   displayName;
    private java.lang.String   avatar;
    private java.lang.String   description;
    
    public java.lang.String getName() {
        return name;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }
    public java.lang.String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }
    public java.lang.String getAvatar() {
        return avatar;
    }
    public void setAvatar(java.lang.String avatar) {
        this.avatar = avatar;
    }
    public java.lang.String getDescription() {
        return description;
    }
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    
}
