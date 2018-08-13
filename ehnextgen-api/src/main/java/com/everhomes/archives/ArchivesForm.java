package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesForms;
import com.everhomes.util.StringHelper;

public class ArchivesForm extends EhArchivesForms {
    private static final long serialVersionUID = -3396396349568392340L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
