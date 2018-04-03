package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingAttachments;
import com.everhomes.util.StringHelper;

public class ParkingAttachment extends EhParkingAttachments{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
