package com.everhomes.enterprise;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseAddresses;

import com.everhomes.util.StringHelper;

public class EnterpriseAddress extends EhEnterpriseAddresses {

    /**
     * 
     */
    private static final long serialVersionUID = -1085190896646182497L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
