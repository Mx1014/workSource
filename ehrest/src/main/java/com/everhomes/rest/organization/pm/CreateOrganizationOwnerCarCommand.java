package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <ul>
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
 *     <li>carAttachments: 附件列表</li>
 * </ul>
 */
public class CreateOrganizationOwnerCarCommand {

    @NotNull private Long communityId;
    @Size(max = 20)
    @NotNull private String plateNumber;
    @NotNull private Long organizationId;
    @Size(max = 20)
    private String brand;
    @Size(max = 20)
    private String parkingSpace;
    private Byte   parkingType;
    @Size(max = 20)
    private String contacts;
    @Size(max = 20)
    private String contactNumber;
    @Size(max = 20)
    private String color;
    @Size(max = 1024)
    private String contentUri;

    @ItemType(UploadOrganizationOwnerCarAttachmentCommand.class)
    private List<UploadOrganizationOwnerCarAttachmentCommand> carAttachments;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public List<UploadOrganizationOwnerCarAttachmentCommand> getCarAttachments() {
        return carAttachments;
    }

    public void setCarAttachments(List<UploadOrganizationOwnerCarAttachmentCommand> carAttachments) {
        this.carAttachments = carAttachments;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
