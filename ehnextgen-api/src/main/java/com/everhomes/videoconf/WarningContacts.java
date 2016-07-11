package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhWarningContacts;
import com.everhomes.util.StringHelper;

public class WarningContacts extends EhWarningContacts {

	private static final long serialVersionUID = -9035334490752638810L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
