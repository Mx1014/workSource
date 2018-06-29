package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>dtos: 会议室列表，参考{@link com.everhomes.rest.meeting.MeetingRoomSimpleInfoDTO}</li>
 * </ul>
 */
public class ListMeetingRoomSimpleInfoResponse {
    private List<MeetingRoomSimpleInfoDTO> dtos;

    public List<MeetingRoomSimpleInfoDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<MeetingRoomSimpleInfoDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
