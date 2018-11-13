package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhCustomerLevelChangeRecords;
import com.everhomes.util.StringHelper;

public class CustomerLevelChangeRecord extends EhCustomerLevelChangeRecords {

    private static final long serialVersionUID = 637600257296271326L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
