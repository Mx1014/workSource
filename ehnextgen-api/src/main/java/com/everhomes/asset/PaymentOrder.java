//@formatter:off
package com.everhomes.asset;

import com.everhomes.server.schema.tables.pojos.EhPaymentOrder;
import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2017/9/13.
 */

public class PaymentOrder extends EhPaymentOrder {
    private static final long serialVersionUID = 4502179757280330900L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
