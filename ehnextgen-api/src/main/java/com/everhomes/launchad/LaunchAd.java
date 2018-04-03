package com.everhomes.launchad;

import com.everhomes.server.schema.tables.pojos.EhLaunchAdvertisements;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2016/12/13.
 */
public class LaunchAd extends EhLaunchAdvertisements {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
