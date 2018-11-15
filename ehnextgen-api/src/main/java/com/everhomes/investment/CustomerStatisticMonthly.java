package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhCustomerStatisticsMonthly;
import com.everhomes.util.StringHelper;

public class CustomerStatisticMonthly extends EhCustomerStatisticsMonthly {

    private static final long serialVersionUID = -7817939261552287804L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
