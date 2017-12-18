// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.server.schema.tables.pojos.EhTasks;
import com.everhomes.util.StringHelper;

public class Task extends EhTasks {

    private static final long serialVersionUID = -2614415176151224956L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
