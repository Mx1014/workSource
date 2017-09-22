package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractChargingItems;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/16.
 */
public class ContractChargingItem extends EhContractChargingItems {
    private static final long serialVersionUID = -2994201534302894333L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
