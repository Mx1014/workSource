package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/8/3.
 */
public class LeaseBuildingDTO {
    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private Long buildingId;
    private String buildingName;
    private String name;
    private String aliasName;
    private String managerName;
    private String managerContact;
    private Double longitude;
    private Double latitude;
    private String address;
    private Double areaSize;
    private String description;
    private String posterUrl;
    private Byte status;
    private String trafficDescription;
    private Long generalFormId;
    private Byte customFormFlag;
    private Long defaultOrder;
    private String detailUrl;
    private Byte deleteFlag;

    @ItemType(BuildingForRentAttachmentDTO.class)
    private List<BuildingForRentAttachmentDTO> attachments;

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public List<BuildingForRentAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<BuildingForRentAttachmentDTO> attachments) {
        this.attachments = attachments;
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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Long getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Long defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
