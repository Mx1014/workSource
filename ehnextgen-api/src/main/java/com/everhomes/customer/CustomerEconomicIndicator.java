package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerEconomicIndicators;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerEconomicIndicator extends EhCustomerEconomicIndicators {
    private static final long serialVersionUID = -9167281962092275255L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
