package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomerAdmins;
import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/5/4 21 :55
 */

public class CustomerAdminRecord extends EhEnterpriseCustomerAdmins {
    private static final long serialVersionUID = 6829215736002934777L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
