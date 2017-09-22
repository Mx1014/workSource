package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerCommercials;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerCommercial extends EhCustomerCommercials {
    private static final long serialVersionUID = 5348855182407408419L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
