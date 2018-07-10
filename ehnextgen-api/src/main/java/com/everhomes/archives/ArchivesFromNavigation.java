package com.everhomes.archives;

import com.everhomes.server.schema.tables.EhArchivesFormNavigation;
import com.everhomes.util.StringHelper;

public class ArchivesFromNavigation extends EhArchivesFormNavigation {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
