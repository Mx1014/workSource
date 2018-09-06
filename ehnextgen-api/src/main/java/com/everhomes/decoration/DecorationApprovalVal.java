package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationApprovalVals;
import com.everhomes.util.StringHelper;

public class DecorationApprovalVal extends EhDecorationApprovalVals {
    private static final long serialVersionUID = 1270449487680076942L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
