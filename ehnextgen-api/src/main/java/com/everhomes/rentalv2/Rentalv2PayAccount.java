package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2PayAccounts;
import com.everhomes.util.StringHelper;

public class Rentalv2PayAccount extends EhRentalv2PayAccounts {
    private static final long serialVersionUID = -7469198887123539099L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
