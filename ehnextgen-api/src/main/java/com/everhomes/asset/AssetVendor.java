package com.everhomes.asset;

import com.everhomes.server.schema.tables.pojos.EhAssetVendor;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/4/11.
 */
public class AssetVendor extends EhAssetVendor {
    private static final long serialVersionUID = -5428366604908778165L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
