package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/3.
 */
public class CreateLeaseBuildingCommand {

    private Long communityId;
    private String name;
    private String aliasName;
    private String managerName;
    private String managerContact;
    private Double longitude;
    private Double latitude;
    private String address;
    private Double areaSize;
    private String description;
    private String posterUri;
    private String trafficDescription;
    private Long generalFormId;
    private Byte customFormFlag;

    @ItemType(BuildingForRentAttachmentDTO.class)
    private List<BuildingForRentAttachmentDTO> attachments;

    @ItemType(PostApprovalFormItem.class)
    private List<PostApprovalFormItem> formValues;

    public List<PostApprovalFormItem> getFormValues() {
        return formValues;
    }

    public void setFormValues(List<PostApprovalFormItem> formValues) {
        this.formValues = formValues;
    }

    public List<BuildingForRentAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<BuildingForRentAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerContact() {
        return managerContact;
    }

    public void setManagerContact(String managerContact) {
        this.managerContact = managerContact;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getTrafficDescription() {
        return trafficDescription;
    }

    public void setTrafficDescription(String trafficDescription) {
        this.trafficDescription = trafficDescription;
    }

    public Long getGeneralFormId() {
        return generalFormId;
    }

    public void setGeneralFormId(Long generalFormId) {
        this.generalFormId = generalFormId;
    }

    public Byte getCustomFormFlag() {
        return customFormFlag;
    }

    public void setCustomFormFlag(Byte customFormFlag) {
        this.customFormFlag = customFormFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
