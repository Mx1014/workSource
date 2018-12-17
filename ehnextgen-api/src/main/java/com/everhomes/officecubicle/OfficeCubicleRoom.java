package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRoom;
import com.everhomes.util.StringHelper;

import java.util.List;

public class OfficeCubicleRoom extends EhOfficeCubicleRoom {



	/**
	 * 
	 */
	private static final long serialVersionUID = 8331710711250008384L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
