package com.everhomes.business;

import com.everhomes.server.schema.tables.pojos.EhBusinessCategories;
import com.everhomes.util.StringHelper;

public class BusinessCategory extends EhBusinessCategories{

    private static final long serialVersionUID = -6999930159666183074L;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
