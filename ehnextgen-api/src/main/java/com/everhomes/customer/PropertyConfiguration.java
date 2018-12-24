package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhPropertyConfigurations;
import com.everhomes.util.StringHelper;

public class PropertyConfiguration extends EhPropertyConfigurations {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
