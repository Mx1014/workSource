package com.everhomes.meeting;

import com.everhomes.server.schema.tables.pojos.EhMeetingInvitationTemplates;
import com.everhomes.util.StringHelper;

public class MeetingInvitationTemplate extends EhMeetingInvitationTemplates {
    private static final long serialVersionUID = 36838464L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
