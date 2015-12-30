package com.everhomes.rest.recommend;

/**
 * <ul> 用户推荐信息
 * <li>id: 用户ID</li>
 * <li>nickName: 用户昵称</li>
 * <li>avatarUri: 头像链接唯一标识</li>
 * <li>avatarUrl: 头像链接</li>
 * <li>communityName: 小区名字</li>
 * <li>description: 辅助描述信息</li>
 * <li>userName: 用户名字</li>
 * <li>userSourceType: 来自小区为0，来自通讯录为1</li>
 * <li>floorRelation: 同楼信息</li>
 * </ul>
 * @author janson
 *
 */
public class RecommendUserInfo {
    private Long id;
    private String nickName;
    private String avatarUri;
    private String avatarUrl;
    private String communityName;
    
    private String description;
    private String userName;
    private Long userSourceType;
    private String floorRelation;
    
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getAvatarUri() {
        return avatarUri;
    }
    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getCommunityName() {
        return communityName;
    }
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
    
    
}
