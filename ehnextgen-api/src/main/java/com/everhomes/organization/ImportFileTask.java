package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhImportFileTasks;
import com.everhomes.util.StringHelper;

/**
 * Created by sfyan on 2017/4/21.
 */
public class ImportFileTask extends EhImportFileTasks{

    private static final long serialVersionUID = 8428338216022084922L;

    public ImportFileTask() {

    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
