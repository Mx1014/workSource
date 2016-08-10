package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleOrders;
import com.everhomes.util.StringHelper;

public class OfficeCubicleOrder extends EhOfficeCubicleOrders {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5901916261055147395L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
