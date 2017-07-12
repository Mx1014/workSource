package com.everhomes.asset;

import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/2/20.
 */
public class AssetBillTemplateFields extends EhAssetBillTemplateFields {
    private static final long serialVersionUID = 4713849916934691492L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
