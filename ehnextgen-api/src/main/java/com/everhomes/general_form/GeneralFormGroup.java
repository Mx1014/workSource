package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormGroups;
import com.everhomes.util.StringHelper;

public class GeneralFormGroups extends EhGeneralFormGroups {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
