package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhTrackingNotifyLogs;
import com.everhomes.util.StringHelper;

public class TrackingNotifyLog extends EhTrackingNotifyLogs {
	
	private static final long serialVersionUID = -8125481508801771513L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
