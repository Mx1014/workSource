package com.everhomes.business;

import com.everhomes.server.schema.tables.pojos.EhBusinessPromotions;
import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2017/1/10.
 */
public class BusinessPromotion extends EhBusinessPromotions {
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
