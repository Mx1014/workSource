package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset : 下一页页码</li>
 * <li>templates: 结果集，参考{@link com.everhomes.rest.meeting.MeetingTemplateDTO}</li>
 * </ul>
 */
public class ListMyMeetingTemplateResponse {
    private Integer nextPageOffset;
    private List<MeetingTemplateDTO> templates;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<MeetingTemplateDTO> getTemplates() {
        return templates;
    }

    public void setTemplates(List<MeetingTemplateDTO> templates) {
        this.templates = templates;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
