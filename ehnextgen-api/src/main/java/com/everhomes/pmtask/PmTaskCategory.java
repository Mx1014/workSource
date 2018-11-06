package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskCategories;
import com.everhomes.util.StringHelper;

public class PmTaskCategory extends EhPmTaskCategories {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
