package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfAccounts;
import com.everhomes.util.StringHelper;

public class ConfAccounts extends EhConfAccounts {

	private static final long serialVersionUID = -655520636517762700L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
