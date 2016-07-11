package com.everhomes.thirdpartuser;

import com.everhomes.server.schema.tables.pojos.EhThirdpartUsers;
import com.everhomes.util.StringHelper;

public class ThirdpartUser extends EhThirdpartUsers {

    /**
     * 
     */
    private static final long serialVersionUID = -2375960525948607667L;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
     }
}
