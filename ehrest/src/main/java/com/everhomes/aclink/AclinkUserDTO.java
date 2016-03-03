package com.everhomes.aclink;

import com.everhomes.util.StringHelper;

public class AclinkUserDTO {
    private Long id;
    private String userName;
    private String phone;
    private String nickName;
    private Byte status;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
