package com.everhomes.rest.blacklist;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 黑名单id</li>
 * <li>userId: 用户id</li>
 * <li>contactName: 名称</li>
 * <li>contactToken: 联系方式</li>
 * <li>gender: 性别， 参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>createTime: 锁定时间</li>
 * </ul>
 */
public class UserBlacklistDTO {

    private Long id;

    private Long userId;

    private String contactName;

    private String contactToken;

    private Byte gender;

    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
