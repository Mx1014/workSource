package com.everhomes.recommend;

import com.everhomes.user.UserInfo;

public class RecommendUserInfo {
    private UserInfo userInfo;
    
    private String description;
    private String userName;
    private Long userSourceType;
    private String floorRelation;
    
    public UserInfo getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getUserSourceType() {
        return userSourceType;
    }
    public void setUserSourceType(Long userSourceType) {
        this.userSourceType = userSourceType;
    }
    public String getFloorRelation() {
        return floorRelation;
    }
    public void setFloorRelation(String floorRelation) {
        this.floorRelation = floorRelation;
    }
    
    
}
