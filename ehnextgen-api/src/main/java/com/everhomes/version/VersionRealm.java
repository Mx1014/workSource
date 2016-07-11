package com.everhomes.version;

import com.everhomes.server.schema.tables.pojos.EhVersionRealm;
import com.everhomes.util.StringHelper;

public class VersionRealm extends EhVersionRealm {
    private static final long serialVersionUID = -2470668610622736751L;

    public VersionRealm() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
