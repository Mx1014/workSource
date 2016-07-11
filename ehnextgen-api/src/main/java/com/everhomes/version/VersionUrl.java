package com.everhomes.version;

import com.everhomes.server.schema.tables.pojos.EhVersionUrls;
import com.everhomes.util.StringHelper;

public class VersionUrl extends EhVersionUrls {
    private static final long serialVersionUID = 8741085837172167003L;

    public VersionUrl() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
