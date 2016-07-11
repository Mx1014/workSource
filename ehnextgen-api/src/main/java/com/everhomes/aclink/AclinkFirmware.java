package com.everhomes.aclink;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhAclinkFirmware;;

public class AclinkFirmware extends EhAclinkFirmware{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4318214784870512158L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
