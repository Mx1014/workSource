package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhCustomerRequirementAddresses;
import com.everhomes.util.StringHelper;

public class CustomerRequirementAddress extends EhCustomerRequirementAddresses {

    private static final long serialVersionUID = 8918938194197326705L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
