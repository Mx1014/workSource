package com.everhomes.rest.community_map;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.ApartmentDTO;

import java.util.List;

/**
 * @author sw on 2017/8/14.
 */
public class CommunityMapBuildingDTO {

    private Long id;

    private String name;

    private String address;

    private Double longitude;

    private Double latitude;

    @ItemType(ApartmentDTO.class)
    private List<ApartmentDTO> apartments;

    public List<ApartmentDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentDTO> apartments) {
        this.apartments = apartments;
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
}
