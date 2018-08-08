package com.everhomes.gogs;

import com.everhomes.server.schema.tables.pojos.EhGogsRepos;
import com.everhomes.util.StringHelper;

public class GogsRepo extends EhGogsRepos {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
