package com.everhomes.contract;

import com.everhomes.server.schema.tables.pojos.EhContractEvents;
import com.everhomes.util.StringHelper;

/**
 * Created by tangcen on 2018/6/3.
 */
public class ContractEvents extends EhContractEvents{
	
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
