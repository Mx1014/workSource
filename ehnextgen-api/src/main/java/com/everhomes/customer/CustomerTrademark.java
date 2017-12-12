package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerTrademarks;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerTrademark extends EhCustomerTrademarks {
    private static final long serialVersionUID = -2736083656641293202L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
