package com.everhomes.rentalv2;


import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderStatistics;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2018/4/18.
 */
public class RentalOrderStatistics extends EhRentalv2OrderStatistics {
    private static final long serialVersionUID = -5961757810672759206L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
