package com.everhomes.relocation;

import com.everhomes.server.schema.tables.pojos.EhRelocationRequests;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/11/20.
 */
public class RelocationRequest extends EhRelocationRequests {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
