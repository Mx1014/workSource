package com.everhomes.archives;

import com.everhomes.server.schema.tables.pojos.EhArchivesFormVals;
import com.everhomes.util.StringHelper;

public class ArchivesFormVal extends EhArchivesFormVals {

    private static final long serialVersionUID = 8372476738610542618L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
