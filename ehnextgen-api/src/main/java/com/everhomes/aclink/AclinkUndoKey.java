package com.everhomes.aclink;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhAclinkUndoKey;

public class AclinkUndoKey extends EhAclinkUndoKey {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4306214886542043887L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
