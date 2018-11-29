package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2PriceClassification;
import com.everhomes.util.StringHelper;

public class RentalPriceClassification extends EhRentalv2PriceClassification {
    private static final long serialVersionUID = 1716010837037075887L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
