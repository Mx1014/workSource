package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerTrackings;
import com.everhomes.util.StringHelper;

/**
 * Created by shengyue.wang on 2017/9/20.
 */
public class CustomerTracking extends EhCustomerTrackings {
	private static final long serialVersionUID = -4070926835158299533L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
