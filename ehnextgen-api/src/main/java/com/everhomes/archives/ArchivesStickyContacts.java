// @formatter:off
package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesStickyContacts;
import com.everhomes.util.StringHelper;

public class ArchivesStickyContacts extends EhArchivesStickyContacts {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
