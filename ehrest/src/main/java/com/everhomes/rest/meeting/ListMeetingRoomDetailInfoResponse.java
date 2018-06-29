package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset : 下一页页码</li>
 * <li>dots: 会议室列表，参考{@link com.everhomes.rest.meeting.MeetingRoomDetailInfoDTO}</li>
 * </ul>
 */
public class ListMeetingRoomDetailInfoResponse {
    private Integer nextPageOffset;
    private List<MeetingRoomDetailInfoDTO> dtos;

    public List<MeetingRoomDetailInfoDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<MeetingRoomDetailInfoDTO> dtos) {
        this.dtos = dtos;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
