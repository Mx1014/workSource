package com.everhomes.meeting;

import com.everhomes.server.schema.tables.pojos.EhMeetingAttachments;
import com.everhomes.util.StringHelper;

public class MeetingAttachment extends EhMeetingAttachments {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2509086649197219372L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
