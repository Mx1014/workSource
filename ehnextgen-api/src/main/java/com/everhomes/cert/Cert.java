package com.everhomes.cert;

import com.everhomes.server.schema.tables.pojos.EhCerts;
import com.everhomes.util.StringHelper;

public class Cert extends EhCerts {

    /**
     * 
     */
    private static final long serialVersionUID = -2608929403634693937L;

    public Cert() {
        
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
