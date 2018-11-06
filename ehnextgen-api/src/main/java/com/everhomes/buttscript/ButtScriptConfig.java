package com.everhomes.buttscript;


import com.everhomes.server.schema.tables.pojos.EhButtScriptConfig;
import com.everhomes.util.StringHelper;

public class ButtScriptConfig extends EhButtScriptConfig {

    public ButtScriptConfig(){

    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
