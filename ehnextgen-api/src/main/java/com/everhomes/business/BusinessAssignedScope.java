package com.everhomes.business;

import com.everhomes.server.schema.tables.pojos.EhBusinessAssignedScopes;
import com.everhomes.util.StringHelper;

public class BusinessAssignedScope extends EhBusinessAssignedScopes{

    private static final long serialVersionUID = -8086202562250912033L;

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }
}
