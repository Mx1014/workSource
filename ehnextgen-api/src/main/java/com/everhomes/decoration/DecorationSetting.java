package com.everhomes.decoration;


import com.everhomes.server.schema.tables.pojos.EhDecorationSetting;
import com.everhomes.util.StringHelper;

public class DecorationSetting extends EhDecorationSetting {

    private static final long serialVersionUID = 4763138642556203182L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
