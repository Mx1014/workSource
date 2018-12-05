package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司ID,必填</li>
 * <li>templateId: 会议模板Id，id为空，默认返回当前用户作为默认参会人和会务人</li>
 * </ul>
 */
public class GetMeetingTemplateCommand {
    private Long organizationId;
    private Long templateId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
