package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID，必填</li>
 * <li>meetingRoomId: 会议室ID，必填</li>
 * <li>queryStartDate: 查询开始日期，不包含时分秒，必填</li>
 * <li>queryEndDate: 查询截止日期，不包含时分秒，必填</li>
 * </ul>
 */
public class GetMeetingRoomDetailCommand {
    private Long organizationId;
    private Long meetingRoomId;
    private Long queryStartDate;
    private Long queryEndDate;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public Long getQueryStartDate() {
        return queryStartDate;
    }

    public void setQueryStartDate(Long queryStartDate) {
        this.queryStartDate = queryStartDate;
    }

    public Long getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(Long queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
