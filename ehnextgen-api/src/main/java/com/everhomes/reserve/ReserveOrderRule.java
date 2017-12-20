package com.everhomes.reserve;

import com.everhomes.server.schema.tables.pojos.EhReserveOrderRules;
import com.everhomes.server.schema.tables.pojos.EhReserveRuleStrategies;
import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class ReserveRuleStrategy extends EhReserveOrderRules {

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
