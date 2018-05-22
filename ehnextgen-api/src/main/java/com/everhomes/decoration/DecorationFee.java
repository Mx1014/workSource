package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationFee;
import com.everhomes.util.StringHelper;

public class DecorationFee extends EhDecorationFee {

    private static final long serialVersionUID = -1296622807878728488L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
