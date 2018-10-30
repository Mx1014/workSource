package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkManagement;

import com.everhomes.util.StringHelper;

public class AclinkManagement extends EhAclinkManagement {
    private static final long serialVersionUID = -3805888675865542774L;

    /**
     * 
     */


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
