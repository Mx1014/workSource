package com.everhomes.rest.community_map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/19.
 */
public class CommunityMapInitDataDTO {

    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private String mapUri;
    private String mapUrl;
    private String mapName;
    private String version;
    private Double centerLongitude;
    private Double centerLatitude;
    private Double northEastLongitude;
    private Double northEastLatitude;
    private Double southWestLongitude;
    private Double southWestLatitude;
    private Double longitudeDelta;
    private Double latitudeDelta;

    @ItemType(CommunityMapBuildingDTO.class)
    private List<CommunityMapBuildingDTO> buildings;

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

    public List<CommunityMapBuildingDTO> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<CommunityMapBuildingDTO> buildings) {
        this.buildings = buildings;
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

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getMapUri() {
        return mapUri;
    }

    public void setMapUri(String mapUri) {
        this.mapUri = mapUri;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public Double getNorthEastLongitude() {
        return northEastLongitude;
    }

    public void setNorthEastLongitude(Double northEastLongitude) {
        this.northEastLongitude = northEastLongitude;
    }

    public Double getSouthWestLatitude() {
        return southWestLatitude;
    }

    public void setSouthWestLatitude(Double southWestLatitude) {
        this.southWestLatitude = southWestLatitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
