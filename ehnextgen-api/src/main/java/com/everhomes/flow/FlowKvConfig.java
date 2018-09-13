package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowKvConfigs;
import com.everhomes.util.StringHelper;

public class FlowKvConfig extends EhFlowKvConfigs {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
