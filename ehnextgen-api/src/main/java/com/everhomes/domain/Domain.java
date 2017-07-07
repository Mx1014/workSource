package com.everhomes.domain;

import com.everhomes.server.schema.tables.pojos.EhDomains;
import com.everhomes.util.StringHelper;


public class Domain extends EhDomains {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
