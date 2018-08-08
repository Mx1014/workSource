// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.server.schema.tables.pojos.EhUserApps;
import com.everhomes.util.StringHelper;


public class UserApp extends EhUserApps {


    private static final long serialVersionUID = -5068328838856695847L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}