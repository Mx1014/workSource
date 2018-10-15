package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkFirmwarePackage;
import com.everhomes.util.StringHelper;

;

public class AclinkFirmwarePackage extends EhAclinkFirmwarePackage{


    private static final long serialVersionUID = 4101259267926582680L;

    /**
     * 
     */

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
