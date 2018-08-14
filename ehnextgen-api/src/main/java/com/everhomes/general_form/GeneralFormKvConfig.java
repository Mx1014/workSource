package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormKvConfigs;
import com.everhomes.util.StringHelper;

public class GeneralFormKvConfig extends EhGeneralFormKvConfigs {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
