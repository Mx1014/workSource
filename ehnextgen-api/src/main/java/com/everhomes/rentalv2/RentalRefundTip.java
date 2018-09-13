package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2RefundTips;
import com.everhomes.util.StringHelper;

public class RentalRefundTip extends EhRentalv2RefundTips {
    private static final long serialVersionUID = -1833067944524791289L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
