package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhCustomerRequirements;
import com.everhomes.util.StringHelper;

public class CustomerRequirement extends EhCustomerRequirements {

    private static final long serialVersionUID = 7590840615951433155L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
