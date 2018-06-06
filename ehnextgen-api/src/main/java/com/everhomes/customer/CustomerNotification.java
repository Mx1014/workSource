//@formatter:off
package com.everhomes.customer;

/**
 * Created by Wentian Wang on 2018/6/5.
 */

public interface CustomerNotification {
    static final String scope = "customer.notification";
    static final int msg_to_new_tracking = 1; //"allocate_new_tracking, when  n != null"
    static final int msg_to_old_tracking = 2; //"when o != null and n != null"
    static final int msg_to_admin_no_candidate = 3; //"when o != null and n == null "
    static final int msg_to_tracking_on_customer_delete = 4; //"when o != null and customerDelete=true "
}
