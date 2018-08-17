//@formatter:off
package com.everhomes.asset;

import com.everhomes.server.schema.tables.pojos.EhPaymentBillItems;
import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2017/8/18.
 */

public class PaymentBillItems extends EhPaymentBillItems {
    private static final long serialVersionUID = 8807959265409473187L;
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
}
