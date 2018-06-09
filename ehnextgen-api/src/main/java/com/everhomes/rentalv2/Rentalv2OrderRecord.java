package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderRecords;
import com.everhomes.util.StringHelper;

public class Rentalv2OrderRecord extends EhRentalv2OrderRecords {
    private static final long serialVersionUID = 743213224654668063L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
