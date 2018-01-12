package com.everhomes.filemanagement;

import com.everhomes.server.schema.tables.pojos.EhFileManagementCatalogs;
import com.everhomes.util.StringHelper;

public class FileCatalog extends EhFileManagementCatalogs{

    private static final long serialVersionUID = -1180376126431274717L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
