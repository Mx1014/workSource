package com.everhomes.launchpad;

import com.everhomes.server.schema.tables.pojos.EhItemServiceCategries;
import com.everhomes.util.StringHelper;

/**
 * Created by sfyan on 2016/10/19.
 */
public class ItemServiceCategry extends EhItemServiceCategries{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
