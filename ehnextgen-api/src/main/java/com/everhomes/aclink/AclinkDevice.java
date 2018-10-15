package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkDevice;
import com.everhomes.util.StringHelper;

;

public class AclinkDevice extends EhAclinkDevice{


    private static final long serialVersionUID = -2387573619585374719L;

    /**
     * 
     */

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
