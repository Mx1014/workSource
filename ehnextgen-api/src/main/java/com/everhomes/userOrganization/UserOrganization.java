package com.everhomes.userOrganization;

import com.everhomes.server.schema.tables.pojos.EhUserOrganization;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/19.
 */
public class UserOrganization extends EhUserOrganization {

    private String userName;

    private Byte gender;

    private String nickName;

    private String phoneNumber;

    private Timestamp registerTime;

    private Byte executiveTag;

    private String position;

    private Long communityId;

    private String identityNumberTag;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Byte getExecutiveTag() {
        return executiveTag;
    }

    public void setExecutiveTag(Byte executiveTag) {
        this.executiveTag = executiveTag;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getIdentityNumberTag() {
        return identityNumberTag;
    }

    public void setIdentityNumberTag(String identityNumberTag) {
        this.identityNumberTag = identityNumberTag;
    }

    public UserOrganization() {
    }
}
