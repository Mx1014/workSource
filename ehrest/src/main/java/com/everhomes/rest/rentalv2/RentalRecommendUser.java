package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>id: id</li>
 * <li>userName: 推荐员姓名</li>
 * <li>mobile: 推荐员手机号</li>
 * <li>iconUri: 推荐员头像uri</li>
 * <li>iconUrl: 推荐员头像url</li>
 * </ul>
 */
public class RentalRecommendUser {
    private Long id;
    private String userName;
    private String mobile;
    private String iconUri;
    private String iconUrl;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
