package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAesServerKey;
import com.everhomes.util.StringHelper;

public class AesServerKey extends EhAesServerKey {
    
    /**
     * 
     */
    private static final long serialVersionUID = 544308245203109735L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
