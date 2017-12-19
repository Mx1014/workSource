package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class SearchReserveOrdersCommand {
    private Long startTime;
    private Long endTime;
    private String spaceNo;
    private Byte status;
    private String applicantEnterpriseName;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getApplicantEnterpriseName() {
        return applicantEnterpriseName;
    }

    public void setApplicantEnterpriseName(String applicantEnterpriseName) {
        this.applicantEnterpriseName = applicantEnterpriseName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
