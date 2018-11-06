package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhCustomerCurrentRents;
import com.everhomes.util.StringHelper;

public class CustomerCurrentRent extends EhCustomerCurrentRents {

    private static final long serialVersionUID = -895067392560505252L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
