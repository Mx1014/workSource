// @formatter:off
package com.everhomes.group;

import com.everhomes.server.schema.tables.pojos.EhGroupOpRequests;
import com.everhomes.util.StringHelper;

public class GroupOpRequest extends EhGroupOpRequests {
    private static final long serialVersionUID = -1879113044544324697L;

    public GroupOpRequest() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
