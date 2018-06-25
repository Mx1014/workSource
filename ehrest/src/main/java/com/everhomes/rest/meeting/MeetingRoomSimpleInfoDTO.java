package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>meetingRoomId: 会议室ID</li>
 * <li>name: 会议室名称</li>
 * <li>seatCount: 会议室座位数</li>
 * </ul>
 */
public class MeetingRoomSimpleInfoDTO {
    private Long meetingRoomId;
    private String name;
    private Integer seatCount;

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
