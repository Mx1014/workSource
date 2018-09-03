package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationWorkers;
import com.everhomes.util.StringHelper;

public class DecorationWorker extends EhDecorationWorkers {
    private static final long serialVersionUID = -5801243340895972192L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
