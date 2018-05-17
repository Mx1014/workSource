package com.everhomes.message;

import com.everhomes.server.schema.tables.pojos.EhMessageRecords;
import com.everhomes.util.StringHelper;

public class MessageRecord extends EhMessageRecords{
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
