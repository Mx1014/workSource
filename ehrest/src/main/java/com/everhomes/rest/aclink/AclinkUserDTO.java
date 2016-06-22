package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 用户ID</li>
 * <li>userName: 用户名</li>
 * <li>phone: 用户手机号</li>
 * <li>authId: 授权对应的授权ID</li>
 * <li>rightOpen: 0 开门未授权， 1 授权</li>
 * <li>rightVisitor: 0 访客未授权， 1 授权</li>
 * <li>rightRemote: 0 远程访问未授权， 1 授权</li>
 * </ul>
 * @author janson
 *
 */
public class AclinkUserDTO {
    private Long id;
    private String userName;
    private String phone;
    private String nickName;
    private Long authId;
    private Byte     rightOpen;
    private Byte     rightVisitor;
    private Byte     rightRemote;
    
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
    public Long getAuthId() {
        return authId;
    }
    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public Byte getRightOpen() {
        return rightOpen;
    }
    public void setRightOpen(Byte rightOpen) {
        this.rightOpen = rightOpen;
    }
    public Byte getRightVisitor() {
        return rightVisitor;
    }
    public void setRightVisitor(Byte rightVisitor) {
        this.rightVisitor = rightVisitor;
    }
    public Byte getRightRemote() {
        return rightRemote;
    }
    public void setRightRemote(Byte rightRemote) {
        this.rightRemote = rightRemote;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
