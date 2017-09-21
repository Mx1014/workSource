// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupConfigures;
import com.everhomes.util.StringHelper;

public class UniongroupConfigures extends EhUniongroupConfigures {

    private static final long serialVersionUID = 5239135408695838848L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}