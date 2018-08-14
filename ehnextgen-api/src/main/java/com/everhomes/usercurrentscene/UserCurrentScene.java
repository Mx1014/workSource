package com.everhomes.usercurrentscene;

import com.everhomes.server.schema.tables.pojos.EhUserCurrentScene;
import com.everhomes.util.StringHelper;

public class UserCurrentScene extends EhUserCurrentScene{

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
