package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskOrders;
import com.everhomes.util.StringHelper;

public class PmTaskOrder extends EhPmTaskOrders {
    private static final long serialVersionUID = 1L;

    public PmTaskOrder() {
        this.setProductFee(0L);
        this.setServiceFee(0L);
        this.setAmount(0L);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
