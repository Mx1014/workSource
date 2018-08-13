package com.everhomes.meeting;

import com.everhomes.server.schema.tables.pojos.EhMeetingRecords;
import com.everhomes.util.StringHelper;

public class MeetingRecord extends EhMeetingRecords {
    private static final long serialVersionUID = 1122923301L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
