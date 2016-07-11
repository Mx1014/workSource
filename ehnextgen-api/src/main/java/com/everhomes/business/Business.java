package com.everhomes.business;

import com.everhomes.server.schema.tables.pojos.EhBusinesses;
import com.everhomes.util.StringHelper;

public class Business extends EhBusinesses{
    private static final long serialVersionUID = -5398593433260480299L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
