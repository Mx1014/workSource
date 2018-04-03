package com.everhomes.rest.community_map;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.community.BuildingAttachmentDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/14.
 */
public class CommunityMapBuildingDTO {

    private Long id;

    private String name;

    private String aliasName;

    private String address;

    private Double centerLongitude;
    private Double centerLatitude;

    @ItemType(CommunityMapBuildingGeoDTO.class)
    private List<CommunityMapBuildingGeoDTO> geos;

    @ItemType(ApartmentDTO.class)
    private List<ApartmentDTO> apartments;

    public List<CommunityMapBuildingGeoDTO> getGeos() {
        return geos;
    }

    public void setGeos(List<CommunityMapBuildingGeoDTO> geos) {
        this.geos = geos;
    }

    public List<ApartmentDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentDTO> apartments) {
        this.apartments = apartments;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
