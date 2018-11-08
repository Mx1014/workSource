package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkGroupDoors;
import com.everhomes.util.StringHelper;

;

public class AclinkGroupDoors extends EhAclinkGroupDoors {

    private static final long serialVersionUID = 7267355771391999643L;

    /**
     * 
     */

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
