package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerCertificates;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/30.
 */
public class CustomerCertificate extends EhCustomerCertificates {
    private static final long serialVersionUID = 7160222958752003222L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
