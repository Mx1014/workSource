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

    private Double longitude;

    private Double latitude;

    private String posterUri;

    private String posterUrl;

    @ItemType(BuildingAttachmentDTO.class)
    private List<BuildingAttachmentDTO> attachments;

    @ItemType(ApartmentDTO.class)
    private List<ApartmentDTO> apartments;

    @ItemType(CommunityMapOrganizationDTO.class)
    private List<CommunityMapOrganizationDTO> organizations;

    @ItemType(CommunityMapShopDTO.class)
    private List<CommunityMapShopDTO> shops;

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

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<BuildingAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<BuildingAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public List<CommunityMapOrganizationDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<CommunityMapOrganizationDTO> organizations) {
        this.organizations = organizations;
    }

    public List<CommunityMapShopDTO> getShops() {
        return shops;
    }

    public void setShops(List<CommunityMapShopDTO> shops) {
        this.shops = shops;
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
