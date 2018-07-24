package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomerAttachments;
import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/5/4 19 :39
 */

public class CustomerAttachements extends EhEnterpriseCustomerAttachments {

    private static final long serialVersionUID = 5413813282692268946L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
