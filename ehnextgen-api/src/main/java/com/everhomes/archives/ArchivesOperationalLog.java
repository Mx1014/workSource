package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesOperationalLogs;
import com.everhomes.util.StringHelper;

public class ArchivesOperationalLog extends EhArchivesOperationalLogs {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
