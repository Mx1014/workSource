package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhAppCategories;
import com.everhomes.util.StringHelper;

public class AppCategory extends EhAppCategories {

    private static final long serialVersionUID = -2974018095036943624L;

    public AppCategory() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
