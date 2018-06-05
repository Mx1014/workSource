//@formatter:off
package com.everhomes.customer;

/**
 * Created by Wentian Wang on 2018/6/5.
 */

public interface CustomerNotification {
    static final String scope = "customer.notification";
    static final int msg_to_new_tracking = 1; //"allocate_new_tracking"
    static final int msg_to_old_tracking = 2; //"cancel old tracking"
    static final int msg_to_old_tracking_no_candidate = 3; //"cancel old tracking"
}
