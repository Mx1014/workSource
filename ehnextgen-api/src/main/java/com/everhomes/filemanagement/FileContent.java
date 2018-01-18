package com.everhomes.filemanagement;

import com.everhomes.server.schema.tables.pojos.EhFileManagementContents;
import com.everhomes.util.StringHelper;

public class FileContent extends EhFileManagementContents{

    private static final long serialVersionUID = -2461286346832193216L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
