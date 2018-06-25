package com.everhomes.customer;

import com.everhomes.server.schema.tables.pojos.EhCustomerAttachments;
import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/6/25 16 :37
 */

public class CustomerAttachment extends EhCustomerAttachments {

    private static final long serialVersionUID = -1526461802161237562L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
