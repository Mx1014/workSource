package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 用户ID</li>
 * <li>userName: 用户名</li>
 * <li>phone: 用户手机号</li>
 * <li>status: 0 未授权， 1 授权</li>
 * <li>authId: 授权对应的授权ID</li>
 * </ul>
 * @author janson
 *
 */
public class AclinkUserDTO {
    private Long id;
    private String userName;
    private String phone;
    private String nickName;
    private Byte status;
    private Long authId;
    
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
    
    public Long getAuthId() {
        return authId;
    }
    public void setAuthId(Long authId) {
        this.authId = authId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
