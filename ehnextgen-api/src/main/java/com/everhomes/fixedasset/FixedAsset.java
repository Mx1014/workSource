package com.everhomes.fixedasset;

import com.everhomes.server.schema.tables.pojos.EhFixedAssets;
import com.everhomes.util.StringHelper;

public class FixedAsset extends EhFixedAssets {
    private static final long serialVersionUID = -125407396L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
