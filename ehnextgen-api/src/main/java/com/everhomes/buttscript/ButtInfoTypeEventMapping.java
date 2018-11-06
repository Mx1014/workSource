package com.everhomes.buttscript;

import com.everhomes.server.schema.tables.pojos.EhButtInfoTypeEventMapping;
import com.everhomes.util.StringHelper;

public class ButtInfoTypeEventMapping extends EhButtInfoTypeEventMapping {

    public ButtInfoTypeEventMapping(){

    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
