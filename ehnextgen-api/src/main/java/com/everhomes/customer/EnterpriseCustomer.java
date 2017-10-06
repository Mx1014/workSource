package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomers;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public class EnterpriseCustomer extends EhEnterpriseCustomers {
    private static final long serialVersionUID = -6128098809813016697L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
