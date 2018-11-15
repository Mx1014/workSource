package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2StructureTemplate;
import com.everhomes.util.StringHelper;

public class RentalStructureTemplate extends EhRentalv2StructureTemplate {
    private static final long serialVersionUID = -2552615813749822486L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
