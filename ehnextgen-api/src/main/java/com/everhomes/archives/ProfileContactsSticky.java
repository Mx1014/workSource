// @formatter:off
package com.everhomes.profile;

import com.everhomes.server.schema.tables.pojos.EhProfileContactsSticky;
import com.everhomes.util.StringHelper;

public class ProfileContactsSticky extends EhProfileContactsSticky {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
