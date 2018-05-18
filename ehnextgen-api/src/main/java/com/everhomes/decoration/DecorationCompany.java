package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationCompanies;
import com.everhomes.util.StringHelper;

public class DecorationCompany extends EhDecorationCompanies {
    private static final long serialVersionUID = 3834759158832915818L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
