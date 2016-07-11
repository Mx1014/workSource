package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfReservations;
import com.everhomes.util.StringHelper;

public class ConfReservations extends EhConfReservations {

	private static final long serialVersionUID = 6051641680190504989L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
