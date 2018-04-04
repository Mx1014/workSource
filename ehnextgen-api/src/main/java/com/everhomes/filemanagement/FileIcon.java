package com.everhomes.filemanagement;

import com.everhomes.server.schema.tables.pojos.EhFileIcons;
import com.everhomes.util.StringHelper;

public class FileIcon extends EhFileIcons{

    private static final long serialVersionUID = -4110186376817226293L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
