// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhUserAppFlags;
import com.everhomes.util.StringHelper;


public class UserAppFlag extends EhUserAppFlags {

    private static final long serialVersionUID = 3276740929235393445L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}