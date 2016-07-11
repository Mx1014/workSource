package com.everhomes.organization.pmsy;

import com.everhomes.server.schema.tables.pojos.EhPmsyOrderItems;
import com.everhomes.util.StringHelper;

public class PmsyOrderItem extends EhPmsyOrderItems{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
