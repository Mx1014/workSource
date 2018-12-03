package com.everhomes.flow_statistics;

import com.everhomes.server.schema.tables.pojos.EhFlowStatisticsHandleLog;
import com.everhomes.util.StringHelper;

public class FlowStatisticsHandleLog extends EhFlowStatisticsHandleLog {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
