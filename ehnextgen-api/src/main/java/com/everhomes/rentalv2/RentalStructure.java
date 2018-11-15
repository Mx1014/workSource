package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2Structures;
import com.everhomes.util.StringHelper;

public class RentalStructure extends EhRentalv2Structures {

    private static final long serialVersionUID = 2628985566026277617L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
