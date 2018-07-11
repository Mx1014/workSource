package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesOperationalConfigurations;
import com.everhomes.util.StringHelper;

public class ArchivesOperationalConfiguration extends EhArchivesOperationalConfigurations {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
