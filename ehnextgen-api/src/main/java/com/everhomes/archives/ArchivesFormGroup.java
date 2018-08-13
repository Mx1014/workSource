package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesFormGroups;
import com.everhomes.util.StringHelper;

public class ArchivesFormGroup extends EhArchivesFormGroups {
    private static final long serialVersionUID = -8703450366260835064L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
