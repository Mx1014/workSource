package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormValRequests;
import com.everhomes.util.StringHelper;

public class GeneralFormValRequest extends EhGeneralFormValRequests {


    private static final long serialVersionUID = 1051198528678316009L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
