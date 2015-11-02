package com.everhomes.enterprise;

/**
 * <ul>修改企业通讯录的基本信息</ul>
 * @author janson
 *
 */
public class UpdateContactCommand {
    private Long id;
    private java.lang.Long     enterpriseId;
    private java.lang.String   name;
    private java.lang.String   nickName;
    private java.lang.String   avatar;
    private java.lang.Long     role;
    private java.lang.Byte     status;
    private java.lang.Long     creatorUid;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public java.lang.Long getRole() {
        return role;
    }
    public void setRole(java.lang.Long role) {
        this.role = role;
    }
    public java.lang.Byte getStatus() {
        return status;
    }
    public void setStatus(java.lang.Byte status) {
        this.status = status;
    }
    public java.lang.Long getCreatorUid() {
        return creatorUid;
    }
    public void setCreatorUid(java.lang.Long creatorUid) {
        this.creatorUid = creatorUid;
    }
}
