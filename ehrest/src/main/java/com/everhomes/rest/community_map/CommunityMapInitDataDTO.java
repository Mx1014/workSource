package com.everhomes.rest.community_map;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/19.
 */
public class CommunityMapInitDataDTO {
    private String mapUrl;
    private String version;
    private List<CommunityMapBuildingDTO> buildingS;

    private Double latitudeDelta;
    private Double longitudeDelta;

    private Double centerLatitude;
    private Double centerLongitude;

    private Double northEastLatitude;
    private Double southWestLongitude;

    public Double getNorthEastLatitude() {
        return northEastLatitude;
    }

    public void setNorthEastLatitude(Double northEastLatitude) {
        this.northEastLatitude = northEastLatitude;
    }

    public Double getSouthWestLongitude() {
        return southWestLongitude;
    }

    public void setSouthWestLongitude(Double southWestLongitude) {
        this.southWestLongitude = southWestLongitude;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<CommunityMapBuildingDTO> getBuildingS() {
        return buildingS;
    }

    public void setBuildingS(List<CommunityMapBuildingDTO> buildingS) {
        this.buildingS = buildingS;
    }

    public Double getLatitudeDelta() {
        return latitudeDelta;
    }

    public void setLatitudeDelta(Double latitudeDelta) {
        this.latitudeDelta = latitudeDelta;
    }

    public Double getLongitudeDelta() {
        return longitudeDelta;
    }

    public void setLongitudeDelta(Double longitudeDelta) {
        this.longitudeDelta = longitudeDelta;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
