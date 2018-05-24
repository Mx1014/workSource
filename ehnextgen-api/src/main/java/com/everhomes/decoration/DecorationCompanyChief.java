package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationCompanyChiefs;
import com.everhomes.util.StringHelper;

public class DecorationCompanyChief extends EhDecorationCompanyChiefs {
    private static final long serialVersionUID = 3750246914601012009L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
