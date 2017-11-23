package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhZjSyncdataBackup;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/8.
 */
public class ZjSyncdataBackup extends EhZjSyncdataBackup {
    private static final long serialVersionUID = -7276585833871448688L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
