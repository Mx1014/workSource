package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleAppProfile;
import com.everhomes.util.StringHelper;

public class ServiceModuleAppProfile extends EhServiceModuleAppProfile {
    /**
     * 
     */
    private static final long serialVersionUID = 7564138789927110803L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
