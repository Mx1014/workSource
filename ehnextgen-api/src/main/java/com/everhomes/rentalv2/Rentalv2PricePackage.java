package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2PricePackages;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/11/7.
 */
public class Rentalv2PricePackage extends EhRentalv2PricePackages {

    private static final long serialVersionUID = 5802634622897128231L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
