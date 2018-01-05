package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhAuthorizationControlConfigs;
import com.everhomes.util.StringHelper;

public class AuthorizationControlConfig extends EhAuthorizationControlConfigs {

    private static final long serialVersionUID = 1195903822123557185L;

    public AuthorizationControlConfig() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
