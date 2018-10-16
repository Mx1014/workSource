package com.everhomes.buttscript;

import com.everhomes.server.schema.tables.pojos.EhButtScriptPublishInfo;
import com.everhomes.util.StringHelper;

public class ButtScriptPublishInfo extends EhButtScriptPublishInfo {


    public ButtScriptPublishInfo(){

    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
