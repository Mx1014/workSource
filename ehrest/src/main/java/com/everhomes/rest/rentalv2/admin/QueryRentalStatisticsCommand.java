package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>orderBy: 排序字段{@link RentalStatisticsOrder}</li>
 * <li>order: 1:顺序 -1:逆序/li>
 * </ul>
 */
public class QueryRentalStatisticsCommand {

    private String resourceType;
    private Long resourceTypeId;
    private Long communityId;
    private Long startDate;
    private Long endDate;

    private Long pageAnchor;

    private Integer pageSize;

    private String orderBy;
    private Integer order;

    private Long currentPMId;
    private Long currentProjectId;
    private Long appId;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getCurrentPMId() {
        return currentPMId;
    }

    public void setCurrentPMId(Long currentPMId) {
        this.currentPMId = currentPMId;
    }

    public Long getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(Long currentProjectId) {
        this.currentProjectId = currentProjectId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }
}
