package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.openapi.UserServiceAddressDTO;

public class GetUserDetailByUuidResponse {
    private String nickName;
    private String mobile;
    private String avatarUrl;
    private Byte gender;
    private String uuid;
    
    //@ItemType(UserServiceAddressDTO.class)
    private UserServiceAddressDTO address;

    public UserServiceAddressDTO getAddress() {
        return address;
    }

    public void setAddress(UserServiceAddressDTO address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
}
