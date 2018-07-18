package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerPotentialDatas;
import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/6/29 14 :59
 */

public class CustomerPotentialData  extends EhCustomerPotentialDatas {
    private static final long serialVersionUID = -2782664610147808757L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
