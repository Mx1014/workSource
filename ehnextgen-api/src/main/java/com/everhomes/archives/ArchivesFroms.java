package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesForms;
import com.everhomes.util.StringHelper;

public class ArchivesFroms extends EhArchivesForms{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
