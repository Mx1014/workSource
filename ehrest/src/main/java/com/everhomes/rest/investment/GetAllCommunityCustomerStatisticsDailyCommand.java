package com.everhomes.rest.investment;

import java.sql.Timestamp;

public class GetAllCommunityCustomerStatisticsDailyCommand {

    private Long orgId;

    private Integer namespaceId;

    private Timestamp startQueryTime;

    private Timestamp endQueryTime;

    private Integer pageSize;

    private Long pageAnchor;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
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

    public Timestamp getStartQueryTime() {
        return startQueryTime;
    }

    public void setStartQueryTime(Timestamp startQueryTime) {
        this.startQueryTime = startQueryTime;
    }

    public Timestamp getEndQueryTime() {
        return endQueryTime;
    }

    public void setEndQueryTime(Timestamp endQueryTime) {
        this.endQueryTime = endQueryTime;
    }
}
