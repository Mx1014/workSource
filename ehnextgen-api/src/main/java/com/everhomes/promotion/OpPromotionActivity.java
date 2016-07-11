package com.everhomes.promotion;

import com.everhomes.server.schema.tables.pojos.EhOpPromotionActivities;
import com.everhomes.util.StringHelper;

public class OpPromotionActivity extends EhOpPromotionActivities {
    /**
     * 
     */
    private static final long serialVersionUID = 2093992672517806550L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
