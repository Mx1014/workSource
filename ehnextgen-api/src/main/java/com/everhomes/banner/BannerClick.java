package com.everhomes.banner;

import com.everhomes.server.schema.tables.pojos.EhBannerClicks;
import com.everhomes.util.StringHelper;

public class BannerClick extends EhBannerClicks {

    private static final long serialVersionUID = -3873795498459613933L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
