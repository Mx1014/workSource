package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerPatents;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerPatent extends EhCustomerPatents {
    private static final long serialVersionUID = -5683755124300955381L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
