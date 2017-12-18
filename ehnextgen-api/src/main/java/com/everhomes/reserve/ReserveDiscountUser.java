package com.everhomes.reserve;

import com.everhomes.server.schema.tables.pojos.EhReserveDiscountUsers;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class ReserveDiscountUser extends EhReserveDiscountUsers{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
