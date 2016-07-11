package com.everhomes.banner;

import com.everhomes.server.schema.tables.pojos.EhBannerOrders;
import com.everhomes.util.StringHelper;

public class BannerOrder extends EhBannerOrders {

    private static final long serialVersionUID = -691438301516932875L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
