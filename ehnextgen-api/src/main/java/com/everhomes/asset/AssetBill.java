package com.everhomes.asset;

import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/2/20.
 */
public class AssetBill extends EhAssetBills{
    private static final long serialVersionUID = -1660420154458509635L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
