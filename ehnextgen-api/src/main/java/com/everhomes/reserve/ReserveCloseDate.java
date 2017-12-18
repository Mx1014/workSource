package com.everhomes.reserve;

import com.everhomes.server.schema.tables.pojos.EhReserveCloseDates;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class ReserveCloseDate extends EhReserveCloseDates {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
