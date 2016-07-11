package com.everhomes.promotion;

import com.everhomes.server.schema.tables.pojos.EhOpPromotionAssignedScopes;
import com.everhomes.util.StringHelper;

public class OpPromotionAssignedScope extends EhOpPromotionAssignedScopes {

    /**
     * 
     */
    private static final long serialVersionUID = 3805460205152736477L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
