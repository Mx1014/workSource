package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesDismissEmployees;
import com.everhomes.util.StringHelper;

public class ArchivesDismissEmployees extends EhArchivesDismissEmployees {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
