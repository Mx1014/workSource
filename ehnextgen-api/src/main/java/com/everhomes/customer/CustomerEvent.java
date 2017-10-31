package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerEvents;
import com.everhomes.util.StringHelper;

/**
 * Created by shengyue.wang on 2017/9/20.
 */
public class CustomerEvent extends EhCustomerEvents {
	private static final long serialVersionUID = 1565293560236571353L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
