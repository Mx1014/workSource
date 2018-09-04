package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2SiteResources;
import com.everhomes.util.StringHelper;

public class RentalResourceFile extends EhRentalv2SiteResources {
    private static final long serialVersionUID = 5188773930894151639L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
