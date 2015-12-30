package com.everhomes.rest.community;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>communityId: 小区id</li>
 * <li>description: 描述</li>
 * <li>longitude: 小区经度</li>
 * <li>latitude: 小区纬度</li>
 * <li>geohash: 根据经纬度生成的geohash</li>
 * </ul>
 */
public class CommunityGeoPointDTO {
    private java.lang.Long   id;
    private java.lang.Long   communityId;
    private java.lang.String description;
    private java.lang.Double longitude;
    private java.lang.Double latitude;
    private java.lang.String geohash;
    
    public java.lang.Long getId() {
        return id;
    }
    public void setId(java.lang.Long id) {
        this.id = id;
    }
    public java.lang.Long getCommunityId() {
        return communityId;
    }
    public void setCommunityId(java.lang.Long communityId) {
        this.communityId = communityId;
    }
    public java.lang.String getDescription() {
        return description;
    }
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    public java.lang.Double getLongitude() {
        return longitude;
    }
    public void setLongitude(java.lang.Double longitude) {
        this.longitude = longitude;
    }
    public java.lang.Double getLatitude() {
        return latitude;
    }
    public void setLatitude(java.lang.Double latitude) {
        this.latitude = latitude;
    }
    public java.lang.String getGeohash() {
        return geohash;
    }
    public void setGeohash(java.lang.String geohash) {
        this.geohash = geohash;
    }
    
}
