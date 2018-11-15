package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhCustomerStatisticsDaily;
import com.everhomes.util.StringHelper;

public class CustomerStatisticDaily extends EhCustomerStatisticsDaily {
    private static final long serialVersionUID = 4760605532435443379L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
