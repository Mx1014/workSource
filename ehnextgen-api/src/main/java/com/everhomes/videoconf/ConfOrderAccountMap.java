package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfOrderAccountMap;
import com.everhomes.util.StringHelper;

public class ConfOrderAccountMap extends EhConfOrderAccountMap {

	private static final long serialVersionUID = -4178786840382782727L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
