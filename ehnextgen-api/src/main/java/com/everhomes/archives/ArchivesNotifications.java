package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesNotifications;
import com.everhomes.util.StringHelper;

public class ArchivesNotifications extends EhArchivesNotifications{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
