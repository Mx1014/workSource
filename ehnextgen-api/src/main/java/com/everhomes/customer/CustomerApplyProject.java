package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerApplyProjects;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerApplyProject extends EhCustomerApplyProjects {
    private static final long serialVersionUID = 7722629800408703816L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
