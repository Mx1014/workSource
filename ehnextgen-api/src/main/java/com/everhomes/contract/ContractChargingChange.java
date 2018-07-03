package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractChargingChanges;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/10/10.
 */
public class ContractChargingChange extends EhContractChargingChanges {
    private static final long serialVersionUID = -9168262042195586844L;
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
}
