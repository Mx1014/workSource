package com.everhomes.fixedasset;

import com.everhomes.server.schema.tables.pojos.EhFixedAssetOperationLogs;
import com.everhomes.util.StringHelper;

public class FixedAssetOperationLog extends EhFixedAssetOperationLogs {
    private static final long serialVersionUID = -151390851L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
