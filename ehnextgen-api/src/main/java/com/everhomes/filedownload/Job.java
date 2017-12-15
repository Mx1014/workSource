// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.server.schema.tables.pojos.EhJobs;
import com.everhomes.util.StringHelper;

public class Job extends EhJobs {

    private static final long serialVersionUID = -833820685215747920L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
