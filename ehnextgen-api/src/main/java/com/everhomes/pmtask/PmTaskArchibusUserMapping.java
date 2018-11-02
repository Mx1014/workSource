package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskArchibusUserMapping;
import com.everhomes.util.StringHelper;

public class PmTaskArchibusUserMapping extends EhPmTaskArchibusUserMapping {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
