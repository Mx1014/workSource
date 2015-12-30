package com.everhomes.rest.user;

/**
 * <ul>
 * <li>此对象只在服务市场的第三方接口用到。</li>
 * </ul>
 * @author janson
 *
 */
public class GetUserByUuidResponse {
    private String nickName;
    private String mobile;
    private String avatarUrl;
    private Byte gender;
    private String uuid;
    
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
