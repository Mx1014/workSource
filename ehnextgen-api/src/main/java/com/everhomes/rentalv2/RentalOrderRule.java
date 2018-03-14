package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderRules;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2018/1/4.
 */
public class RentalOrderRule extends EhRentalv2OrderRules{
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
