package com.everhomes.meeting;

import com.everhomes.server.schema.tables.pojos.EhMeetingReservations;
import com.everhomes.util.StringHelper;

public class MeetingReservation extends EhMeetingReservations {
    private static final long serialVersionUID = 1501206851L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
