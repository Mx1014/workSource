package com.everhomes.banner;

import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.util.StringHelper;

public class Banner extends EhBanners {

    private static final long serialVersionUID = -4764086441501363733L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
