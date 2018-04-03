package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerTrackingPlans;
import com.everhomes.util.StringHelper;

/**
 * Created by shengyue.wang on 2017/9/20.
 */
public class CustomerTrackingPlan extends EhCustomerTrackingPlans {
	private static final long serialVersionUID = -1183326848935000282L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
