// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.server.schema.tables.pojos.EhSensitiveFilterRecord;
import com.everhomes.util.StringHelper;

public class SensitiveFilterRecord extends EhSensitiveFilterRecord{

    private static final long serialVersionUID = 4355403245208067624L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
