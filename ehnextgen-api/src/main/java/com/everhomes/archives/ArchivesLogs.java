package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesLogs;
import com.everhomes.util.StringHelper;

public class ArchivesLogs extends EhArchivesLogs{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
