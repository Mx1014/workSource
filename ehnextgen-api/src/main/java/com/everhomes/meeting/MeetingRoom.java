package com.everhomes.meeting;

import com.everhomes.server.schema.tables.pojos.EhMeetingRooms;
import com.everhomes.util.StringHelper;

public class MeetingRoom extends EhMeetingRooms {
    private static final long serialVersionUID = 1141772521L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
