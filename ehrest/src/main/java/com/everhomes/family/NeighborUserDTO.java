// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 邻居用户Id</li>
 * <li>userName: 邻居用户名称</li>
 * <li>userAvatarUri: 邻居用户头像Id</li>
 * <li>userAvatarUrl: 邻居用户头像url</li>
 * <li>userStatusLine: 个性签名</li>
 * <li>neighborhoodRelation: 邻居关系0-未知、1-同层、2-同楼</li>
 * <li>distance: 邻居用户与自己的距离</li>
 * </ul>
 */
public class NeighborUserDTO {
    private Long userId;
    private String userName;
    private String userAvatarUri;
    private String userAvatarUrl;
    private String userStatusLine;
    private Byte neighborhoodRelation;
    private Double distance;

    public NeighborUserDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarUri() {
        return userAvatarUri;
    }

    public void setUserAvatarUri(String userAvatarUri) {
        this.userAvatarUri = userAvatarUri;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserStatusLine() {
        return userStatusLine;
    }

    public void setUserStatusLine(String userStatusLine) {
        this.userStatusLine = userStatusLine;
    }

    public Byte getNeighborhoodRelation() {
        return neighborhoodRelation;
    }

    public void setNeighborhoodRelation(Byte neighborhoodRelation) {
        this.neighborhoodRelation = neighborhoodRelation;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
