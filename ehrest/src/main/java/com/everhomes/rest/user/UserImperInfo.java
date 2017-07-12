package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class UserImperInfo {
    private Long id;
    private Long imperId;
    private Integer namespaceId;
    private String nickName;
    private String phone;
    private String targetPhone;
    private Byte gender;
    private Long createTime;
    private String description;
    private Long ownerId;
    private Long targetId;

    public Long getImperId() {
        return imperId;
    }
    public void setImperId(Long imperId) {
        this.imperId = imperId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getTargetPhone() {
        return targetPhone;
    }
    public void setTargetPhone(String targetPhone) {
        this.targetPhone = targetPhone;
    }
    public Byte getGender() {
        return gender;
    }
    public void setGender(Byte gender) {
        this.gender = gender;
    }
    public Long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
