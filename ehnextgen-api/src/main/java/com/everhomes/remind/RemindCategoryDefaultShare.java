package com.everhomes.remind;

import com.everhomes.server.schema.tables.pojos.EhRemindCategoryDefaultShares;
import com.everhomes.util.StringHelper;

public class RemindCategoryDefaultShare extends EhRemindCategoryDefaultShares {
    private static final long serialVersionUID = -1827854857L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
