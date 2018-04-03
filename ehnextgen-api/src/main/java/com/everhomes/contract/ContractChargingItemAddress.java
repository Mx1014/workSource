package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractChargingItemAddresses;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/8/16.
 */
public class ContractChargingItemAddress extends EhContractChargingItemAddresses {
    private static final long serialVersionUID = 5639980640698383845L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
