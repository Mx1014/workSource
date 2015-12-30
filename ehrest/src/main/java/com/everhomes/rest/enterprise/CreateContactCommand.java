package com.everhomes.rest.enterprise;

/**
 * <ul>非注册流程，后台管理员创建企业通讯录</ul>
 * @author janson
 *
 */
public class CreateContactCommand {
    private java.lang.Long     enterpriseId;
    private java.lang.String   name;
    private java.lang.String   nickName;
    private java.lang.String   avatar;
    
    public java.lang.Long getEnterpriseId() {
        return enterpriseId;
    }
    public void setEnterpriseId(java.lang.Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    public java.lang.String getName() {
        return name;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }
    public java.lang.String getNickName() {
        return nickName;
    }
    public void setNickName(java.lang.String nickName) {
        this.nickName = nickName;
    }
    public java.lang.String getAvatar() {
        return avatar;
    }
    public void setAvatar(java.lang.String avatar) {
        this.avatar = avatar;
    }
    
    
}
