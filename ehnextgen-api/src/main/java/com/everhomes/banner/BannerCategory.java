// @formatter:off
package com.everhomes.banner;

import com.everhomes.server.schema.tables.pojos.EhBannerCategories;
import com.everhomes.util.StringHelper;

public class BannerCategory extends EhBannerCategories{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
