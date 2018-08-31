package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationRequests;
import com.everhomes.util.StringHelper;

public class DecorationRequest extends EhDecorationRequests {
    private static final long serialVersionUID = 5824498645288803885L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
