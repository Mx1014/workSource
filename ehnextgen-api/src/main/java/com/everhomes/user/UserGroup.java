// @formatter:off
package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhUserGroups;
import com.everhomes.util.StringHelper;

public class UserGroup extends EhUserGroups {
    private static final long serialVersionUID = -7625998854069061271L;

    public UserGroup() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
