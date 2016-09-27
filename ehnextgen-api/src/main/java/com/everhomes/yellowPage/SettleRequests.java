package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhSettleRequests;
import com.everhomes.util.StringHelper;

public class SettleRequests extends EhSettleRequests {

	private static final long serialVersionUID = 6908957167676232045L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
