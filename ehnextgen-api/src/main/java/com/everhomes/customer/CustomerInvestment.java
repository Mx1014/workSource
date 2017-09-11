package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerInvestments;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerInvestment extends EhCustomerInvestments {
    private static final long serialVersionUID = -3876852245437628249L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
