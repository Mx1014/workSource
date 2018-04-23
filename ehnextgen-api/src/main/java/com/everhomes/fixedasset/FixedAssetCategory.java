package com.everhomes.fixedasset;

import com.everhomes.server.schema.tables.pojos.EhFixedAssetCategories;
import com.everhomes.util.StringHelper;

public class FixedAssetCategory extends EhFixedAssetCategories {
    private static final long serialVersionUID = 1788238487L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
