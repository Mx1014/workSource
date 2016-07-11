package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAesUserKey;
import com.everhomes.util.StringHelper;

public class AesUserKey extends EhAesUserKey {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2626110227581910042L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
