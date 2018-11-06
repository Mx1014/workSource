package com.everhomes.buttscript;

import com.everhomes.server.schema.tables.pojos.EhButtScriptLastCommit;
import com.everhomes.util.StringHelper;

public class ButtScriptLastCommit extends EhButtScriptLastCommit {

    public ButtScriptLastCommit(){

    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
