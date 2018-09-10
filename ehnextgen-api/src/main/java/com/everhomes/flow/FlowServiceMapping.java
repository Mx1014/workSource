package com.everhomes.flow;

import com.everhomes.server.schema.tables.pojos.EhFlowServiceMappings;
import com.everhomes.util.StringHelper;

public class FlowServiceMapping extends EhFlowServiceMappings {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
