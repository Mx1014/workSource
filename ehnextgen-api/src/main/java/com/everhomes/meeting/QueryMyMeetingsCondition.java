package com.everhomes.meeting;

import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;

public class QueryMyMeetingsCondition {
    private Integer namespaceId;
    private Long organizationId;
    private boolean endFlag;
    private Long detailId;
    private Date betweenFromDate;
    private Date betweenToDate;
    private Integer pageSize;
    private Integer offset;

    public QueryMyMeetingsCondition() {
    }

    public QueryMyMeetingsCondition(Integer namespaceId, Long organizationId, boolean endFlag, Long detailId, Integer pageSize, Integer offset) {
        this.namespaceId = namespaceId;
        this.organizationId = organizationId;
        this.endFlag = endFlag;
        this.detailId = detailId;
        this.pageSize = pageSize;
        this.offset = offset;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isEndFlag() {
        return endFlag;
    }

    public void setEndFlag(boolean endFlag) {
        this.endFlag = endFlag;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Date getBetweenFromDate() {
        return betweenFromDate;
    }

    public void setBetweenFromDate(Date betweenFromDate) {
        this.betweenFromDate = betweenFromDate;
    }

    public Date getBetweenToDate() {
        return betweenToDate;
    }

    public void setBetweenToDate(Date betweenToDate) {
        this.betweenToDate = betweenToDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
