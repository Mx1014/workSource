package com.everhomes.profile;

import com.everhomes.server.schema.tables.pojos.EhProfileDismissEmployees;
import com.everhomes.util.StringHelper;

public class ProfileDismissEmployees extends EhProfileDismissEmployees{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
