package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset : 下一页页码</li>
 * <li>dtos: 结果集，参考{@link com.everhomes.rest.meeting.MeetingDashboardDTO}</li>
 * </ul>
 */
public class ListMyUnfinishedMeetingResponse {
    private Integer nextPageOffset;
    private List<MeetingDashboardDTO> dtos;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<MeetingDashboardDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<MeetingDashboardDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
