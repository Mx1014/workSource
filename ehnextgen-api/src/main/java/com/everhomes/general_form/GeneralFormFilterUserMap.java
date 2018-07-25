package com.everhomes.general_form;

import com.everhomes.server.schema.tables.pojos.EhGeneralFormFilterUserMap;
import com.everhomes.util.StringHelper;

public class GeneralFormFilterUserMap extends EhGeneralFormFilterUserMap {

    private static final long serialVersionUID = -8550630138682400252L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
