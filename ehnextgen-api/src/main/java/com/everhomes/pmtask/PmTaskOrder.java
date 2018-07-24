package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskOrders;
import com.everhomes.util.StringHelper;

public class PmTaskOrder extends EhPmTaskOrders {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
