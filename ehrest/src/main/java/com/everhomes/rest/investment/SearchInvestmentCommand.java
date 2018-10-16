package com.everhomes.rest.investment;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class SearchInvestmentCommand {
    private Long ownerId;

    private String ownerType;

    private String keyword;

    private String trackingName;

    private Long customerCategoryId;

    private String levelId;

    private Long corpIndustryItemId;

    private Long communityId;

    private Long pageAnchor;

    private Integer pageSize;

    private Long trackingUid;

    private Integer type;

    private Integer lastTrackingTime;

    private String propertyType;

    private String propertyUnitPrice;

    private String propertyArea;

    private Integer sortType;

    private String sortField;

    private Integer namespaceId;

    private Long orgId;

    @ItemType(Long.class)
    private List<Long> trackingUids;

    private Byte adminFlag;

    private Long buildingId;

    private Long addressId;

    private Long sourceItemId;

    private String sourceType;

    private Byte aptitudeFlagItemId;

    private Byte abnormalFlag;

    private Byte ContractSearchCustomerFlag;

    private Long taskId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTrackingName() {
        return trackingName;
    }

    public void setTrackingName(String trackingName) {
        this.trackingName = trackingName;
    }

    public Long getCustomerCategoryId() {
        return customerCategoryId;
    }

    public void setCustomerCategoryId(Long customerCategoryId) {
        this.customerCategoryId = customerCategoryId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public Long getCorpIndustryItemId() {
        return corpIndustryItemId;
    }

    public void setCorpIndustryItemId(Long corpIndustryItemId) {
        this.corpIndustryItemId = corpIndustryItemId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTrackingUid() {
        return trackingUid;
    }

    public void setTrackingUid(Long trackingUid) {
        this.trackingUid = trackingUid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLastTrackingTime() {
        return lastTrackingTime;
    }

    public void setLastTrackingTime(Integer lastTrackingTime) {
        this.lastTrackingTime = lastTrackingTime;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyUnitPrice() {
        return propertyUnitPrice;
    }

    public void setPropertyUnitPrice(String propertyUnitPrice) {
        this.propertyUnitPrice = propertyUnitPrice;
    }

    public String getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(String propertyArea) {
        this.propertyArea = propertyArea;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public List<Long> getTrackingUids() {
        return trackingUids;
    }

    public void setTrackingUids(List<Long> trackingUids) {
        this.trackingUids = trackingUids;
    }

    public Byte getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(Byte adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getSourceItemId() {
        return sourceItemId;
    }

    public void setSourceItemId(Long sourceItemId) {
        this.sourceItemId = sourceItemId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Byte getAptitudeFlagItemId() {
        return aptitudeFlagItemId;
    }

    public void setAptitudeFlagItemId(Byte aptitudeFlagItemId) {
        this.aptitudeFlagItemId = aptitudeFlagItemId;
    }

    public Byte getAbnormalFlag() {
        return abnormalFlag;
    }

    public void setAbnormalFlag(Byte abnormalFlag) {
        this.abnormalFlag = abnormalFlag;
    }

    public Byte getContractSearchCustomerFlag() {
        return ContractSearchCustomerFlag;
    }

    public void setContractSearchCustomerFlag(Byte contractSearchCustomerFlag) {
        ContractSearchCustomerFlag = contractSearchCustomerFlag;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
