package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>meetingRoomId: 会议室ID</li>
 * <li>meetingDate: 会议日期时间戳，不包含时分秒</li>
 * <li>cellBeginTime: 单元格起始时间戳(毫秒数)，如6:00的时间戳等于6*3600*1000</li>
 * <li>cellEndTime: 单元格结束时间戳(毫秒数)，如06:15的时间戳等于6*3600*1000+15*60*1000</li>
 * </ul>
 */
public class GetMeetingReservationSimpleByTimeUnitCommand {
    private Long organizationId;
    private Long meetingRoomId;
    private Long meetingDate;
    private Long cellBeginTime;
    private Long cellEndTime;


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

    public Long getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Long meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Long getCellBeginTime() {
        return cellBeginTime;
    }

    public void setCellBeginTime(Long cellBeginTime) {
        this.cellBeginTime = cellBeginTime;
    }

    public Long getCellEndTime() {
        return cellEndTime;
    }

    public void setCellEndTime(Long cellEndTime) {
        this.cellEndTime = cellEndTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
