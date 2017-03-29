package com.everhomes.asset;

import com.everhomes.server.schema.tables.pojos.EhAssetBillNotifyRecords;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/3/2.
 */
public class AssetBillNotifyRecords extends EhAssetBillNotifyRecords {

    private static final long serialVersionUID = -5843741760007918L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
