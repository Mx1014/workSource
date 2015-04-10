// @formatter:off
package com.everhomes.family;

public class NeighborUserDTO {
    private Long userId;
    private String userName;
    private String userAvatar;
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

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
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
}
