package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkFormValues;
import com.everhomes.util.StringHelper;

;

public class AclinkFormValues extends EhAclinkFormValues {


    private static final long serialVersionUID = -5484304901446862154L;

    /**
     * 
     */

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
