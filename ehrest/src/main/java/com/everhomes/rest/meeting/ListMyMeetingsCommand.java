package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>endFlag: 会议是否已结束,0 未结束，1 已结束，参考{@link com.everhomes.rest.meeting.MeetingGeneralFlag}</li>
 * <li>pageOffset: 页码，默认为1，和pageSize都不传时不分页</li>
 * <li>pageSize: 每页返回记录数，和pageOffset都不传时不分页,分页的话默认传50</li>
 * </ul>
 */
public class ListMyMeetingsCommand {
    private Long organizationId;
    private Byte endFlag;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(Byte endFlag) {
        this.endFlag = endFlag;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
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
