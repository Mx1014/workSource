package com.everhomes.remind;

import com.everhomes.server.schema.tables.pojos.EhRemindCategories;
import com.everhomes.util.StringHelper;

public class RemindCategory extends EhRemindCategories {
    private static final long serialVersionUID = -1575007092L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
