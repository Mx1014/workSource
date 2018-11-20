package com.everhomes.rest.investment;

import java.sql.Timestamp;
import java.util.List;

public class GetCustomerStatisticsCommand {

    private Long orgId;

    private Integer namespaceId;

    private Long startQueryTime;

    private Long endQueryTime;

    private List<Long> communities;

    private Integer pageSize;

    private Integer pageAnchor;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Integer pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getStartQueryTime() {
        return startQueryTime;
    }

    public void setStartQueryTime(Long startQueryTime) {
        this.startQueryTime = startQueryTime;
    }

    public Long getEndQueryTime() {
        return endQueryTime;
    }

    public void setEndQueryTime(Long endQueryTime) {
        this.endQueryTime = endQueryTime;
    }

    public List<Long> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Long> communities) {
        this.communities = communities;
    }

}
