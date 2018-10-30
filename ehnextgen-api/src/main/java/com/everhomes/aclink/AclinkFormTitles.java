package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkFirmwareNew;
import com.everhomes.server.schema.tables.pojos.EhAclinkFormTitles;
import com.everhomes.util.StringHelper;

;

public class AclinkFormTitles extends EhAclinkFormTitles {
    private static final long serialVersionUID = -4939639183743641935L;


    /**
     * 
     */

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
