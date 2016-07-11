package com.everhomes.business;

import com.everhomes.server.schema.tables.pojos.EhBusinessAssignedNamespaces;
import com.everhomes.util.StringHelper;

public class BusinessAssignedNamespace extends EhBusinessAssignedNamespaces{

	private static final long serialVersionUID = -4039513976911575081L;

	@Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }
}
