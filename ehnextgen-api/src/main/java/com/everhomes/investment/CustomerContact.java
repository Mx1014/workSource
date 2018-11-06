package com.everhomes.investment;

import com.everhomes.server.schema.tables.pojos.EhCustomerContacts;
import com.everhomes.util.StringHelper;

public class CustomerContact extends EhCustomerContacts {


    private static final long serialVersionUID = -7555714930607266248L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
