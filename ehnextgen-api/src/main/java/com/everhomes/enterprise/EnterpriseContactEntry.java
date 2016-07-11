package com.everhomes.enterprise;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactEntries;
import com.everhomes.util.StringHelper;

public class EnterpriseContactEntry extends EhEnterpriseContactEntries {
    /**
     * 
     */
    private static final long serialVersionUID = 6305555766167455199L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
