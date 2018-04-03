package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerTalents;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerTalent extends EhCustomerTalents {
    private static final long serialVersionUID = -748766340438135331L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
