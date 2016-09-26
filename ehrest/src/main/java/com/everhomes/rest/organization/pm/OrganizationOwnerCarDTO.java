package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import java.util.Objects;

/**
 * <ul>
 *     <li>id: carId</li>
 *     <li>brand: 品牌</li>
 *     <li>plateNumber: 车牌号</li>
 *     <li>parkingSpace: 停车位</li>
 *     <li>parkingType: 停车类型</li>
 *     <li>contacts: 联系人</li>
 *     <li>contactNumber: 联系人电话</li>
 *     <li>color: 颜色</li>
 *     <li>contentUri: 图片的URI</li>
 *     <li>contentUrl: 图片的URL</li>
 * </ul>
 */
public class OrganizationOwnerCarDTO {

    private Long   id;
    private String brand;
    private String plateNumber;
    private String parkingSpace;
    private String parkingType;
    private String contacts;
    private String contactNumber;
    private String color;
    private String contentUri;
    private String contentUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(String parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof OrganizationOwnerCarDTO
                && Objects.equals(this.getId(), ((OrganizationOwnerCarDTO) obj).getId());
    }

    @Override
    public int hashCode() {
        if (this.getId() != null)
            return this.getId().hashCode();
        return super.hashCode();
    }
}
