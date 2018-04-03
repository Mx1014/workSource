package com.everhomes.objectstorage;

import com.everhomes.server.schema.tables.pojos.EhOsObjects;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/2/17.
 */
public class OsObject extends EhOsObjects {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
