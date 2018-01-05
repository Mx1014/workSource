package com.everhomes.relocation;

import com.everhomes.server.schema.tables.pojos.EhRelocationRequestItems;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/11/20.
 */
public class RelocationRequestItem extends EhRelocationRequestItems {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
