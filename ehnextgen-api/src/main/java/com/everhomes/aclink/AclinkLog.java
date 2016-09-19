package com.everhomes.aclink;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhAclinkLogs;

public class AclinkLog extends EhAclinkLogs {
    /**
     * 
     */
    private static final long serialVersionUID = -3162979816726667992L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
