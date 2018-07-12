package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset : 下一页页码</li>
 * <li>dtos: 结果集，参考{@link com.everhomes.rest.meeting.MeetingReservationSimpleDTO}</li>
 * </ul>
 */
public class ListMyMeetingsResponse {
    private Integer nextPageOffset;
    private List<MeetingReservationSimpleDTO> dtos;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<MeetingReservationSimpleDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<MeetingReservationSimpleDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
