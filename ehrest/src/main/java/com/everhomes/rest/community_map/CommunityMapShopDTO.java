package com.everhomes.rest.community_map;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/14.
 */
public class CommunityMapShopDTO {

    private String shopNo;
    private String shopName;
    private String shopLogo;
    private String appUserShopUrl;

    private Long searchTypeId;
    private String searchTypeName;
    private String contentType;

    private Double longitude;
    private Double latitude;

    private String buildingName;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getAppUserShopUrl() {
        return appUserShopUrl;
    }

    public void setAppUserShopUrl(String appUserShopUrl) {
        this.appUserShopUrl = appUserShopUrl;
    }

    public Long getSearchTypeId() {
        return searchTypeId;
    }

    public void setSearchTypeId(Long searchTypeId) {
        this.searchTypeId = searchTypeId;
    }

    public String getSearchTypeName() {
        return searchTypeName;
    }

    public void setSearchTypeName(String searchTypeName) {
        this.searchTypeName = searchTypeName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
