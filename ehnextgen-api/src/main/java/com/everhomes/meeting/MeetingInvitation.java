package com.everhomes.meeting;

import com.everhomes.server.schema.tables.pojos.EhMeetingInvitations;
import com.everhomes.util.StringHelper;

public class MeetingInvitation extends EhMeetingInvitations {
    private static final long serialVersionUID = -1184664001L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
