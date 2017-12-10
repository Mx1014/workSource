// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.group.Group;
import com.everhomes.group.GroupCustomField;
import com.everhomes.server.schema.tables.pojos.EhFileDownloadJobs;
import com.everhomes.util.StringHelper;

public class FileDownloadJob extends EhFileDownloadJobs {

    private static final long serialVersionUID = 7086392176918058901L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
