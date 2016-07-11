package com.everhomes.promotion;

import com.everhomes.server.schema.tables.pojos.EhOpPromotionMessages;
import com.everhomes.util.StringHelper;

public class OpPromotionMessage extends EhOpPromotionMessages {
    /**
     * 
     */
    private static final long serialVersionUID = 3629796054652439084L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
