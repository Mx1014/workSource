package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceReservationRequests;
import com.everhomes.util.StringHelper;

public class ReservationRequests extends EhServiceAllianceReservationRequests{

	private static final long serialVersionUID = -2201469549904321477L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
