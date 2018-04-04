package com.everhomes.filemanagement;

import com.everhomes.server.schema.tables.pojos.EhFileManagementCatalogScopes;
import com.everhomes.util.StringHelper;

public class FileCatalogScope extends EhFileManagementCatalogScopes{

    private static final long serialVersionUID = -5439587757630198189L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
