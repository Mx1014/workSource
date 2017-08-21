// @formatter:off
package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesContactsSticky;
import com.everhomes.util.StringHelper;

public class ArchivesContactsSticky extends EhArchivesContactsSticky {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
