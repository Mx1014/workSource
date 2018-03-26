package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleAppAuthorizations;
import com.everhomes.util.StringHelper;

public class ServiceModuleAppAuthorization extends EhServiceModuleAppAuthorizations {
    /**
     * 
     */
    private static final long serialVersionUID = 8922507721762481175L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
