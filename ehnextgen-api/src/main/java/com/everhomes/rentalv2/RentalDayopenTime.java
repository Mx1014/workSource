package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2DayopenTime;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2018/4/19.
 */
public class RentalDayopenTime extends EhRentalv2DayopenTime {
    private static final long serialVersionUID = -5688888781853825252L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
