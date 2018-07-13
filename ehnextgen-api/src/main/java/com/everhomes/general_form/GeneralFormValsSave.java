package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormValsSave;
import com.everhomes.util.StringHelper;

public class GeneralFormValsSave extends EhGeneralFormValsSave {


    private static final long serialVersionUID = 1051198528678316009L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
