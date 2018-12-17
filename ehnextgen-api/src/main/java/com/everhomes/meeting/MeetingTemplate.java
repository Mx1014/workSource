package com.everhomes.meeting;

import com.everhomes.server.schema.tables.pojos.EhMeetingTemplates;
import com.everhomes.util.StringHelper;

public class MeetingTemplate extends EhMeetingTemplates {
    private static final long serialVersionUID = 53600243L;

    public MeetingTemplate() {

    }

    public MeetingTemplate(Integer namespaceId, Long organizationId, Long userId, Long detailId) {
        this.setNamespaceId(namespaceId);
        this.setOrganizationId(organizationId);
        this.setUserId(userId);
        this.setDetailId(detailId);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
