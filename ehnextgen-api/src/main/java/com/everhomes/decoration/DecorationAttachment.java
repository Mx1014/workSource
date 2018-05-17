package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationAtttachment;
import com.everhomes.util.StringHelper;

public class DecorationAttachment extends EhDecorationAtttachment {
    private static final long serialVersionUID = -611284638521881410L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
