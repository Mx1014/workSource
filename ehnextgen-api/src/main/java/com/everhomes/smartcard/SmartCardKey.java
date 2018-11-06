package com.everhomes.smartcard;

import com.everhomes.server.schema.tables.pojos.EhSmartCardKeys;
import com.everhomes.util.StringHelper;

public class SmartCardKey extends EhSmartCardKeys {
    /**
     * 
     */
    private static final long serialVersionUID = -6786266995901606729L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
