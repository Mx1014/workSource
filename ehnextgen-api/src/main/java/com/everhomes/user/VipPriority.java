// @formatter:off
package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhVipPriority;
import com.everhomes.util.StringHelper;

public class VipPriority extends EhVipPriority{
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
