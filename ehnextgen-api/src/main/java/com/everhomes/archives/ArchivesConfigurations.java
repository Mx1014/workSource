package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesConfigurations;
import com.everhomes.util.StringHelper;

public class ArchivesConfigurations extends EhArchivesConfigurations{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
