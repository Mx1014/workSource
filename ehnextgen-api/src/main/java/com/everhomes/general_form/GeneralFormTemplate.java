package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormTemplates;
import com.everhomes.util.StringHelper;

public class GeneralFormTemplate extends EhGeneralFormTemplates{

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
