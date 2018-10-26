package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkFormTitles;
import com.everhomes.server.schema.tables.pojos.EhAclinkGroup;
import com.everhomes.util.StringHelper;

;

public class AclinkGroup extends EhAclinkGroup {


    private static final long serialVersionUID = 5398249543889514497L;

    /**
     * 
     */

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
