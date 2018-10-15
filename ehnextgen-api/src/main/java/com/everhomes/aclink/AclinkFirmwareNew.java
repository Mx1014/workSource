package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkFirmwareNew;
import com.everhomes.util.StringHelper;

;

public class AclinkFirmwareNew extends EhAclinkFirmwareNew{

    private static final long serialVersionUID = -8295773892528329058L;

    /**
     * 
     */

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
