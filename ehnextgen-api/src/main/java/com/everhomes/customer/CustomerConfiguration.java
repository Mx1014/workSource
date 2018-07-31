package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerConfigutations;
import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/6/29 10 :21
 */

public class CustomerConfiguration extends EhCustomerConfigutations {
    private static final long serialVersionUID = -6166343045477936208L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
