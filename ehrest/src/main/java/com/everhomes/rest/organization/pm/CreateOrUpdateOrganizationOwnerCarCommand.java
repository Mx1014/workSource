package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>id: carId</li>
 *     <li>organizationId: 公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>brand: 品牌</li>
 *     <li>plateNumber: 车牌号</li>
 *     <li>parkingSpace: 停车位</li>
 *     <li>parkingType: 停车类型 {@link com.everhomes.rest.organization.pm.OrganizationOwnerCarParkingType}</li>
 *     <li>contacts: 联系人</li>
 *     <li>contactNumber: 联系人电话</li>
 *     <li>color: 颜色</li>
 *     <li>contentUri: 图片的URI</li>
 *     <li>contentUrl: 图片的URL</li>
 * </ul>
 */
public class CreateOrUpdateOrganizationOwnerCarCommand {

    private Long   id;
    private Long   communityId;
    private String brand;
    private String plateNumber;
    private String parkingSpace;
    private Byte   parkingType;
    private String contacts;
    private String contactNumber;
    private String color;
    private String contentUri;
    private Long   organizationId;

    public Long getId() {
        return id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    public Byte getParkingType() {
        return parkingType;
    }

    public void setParkingType(Byte parkingType) {
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
}
