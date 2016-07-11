package com.everhomes.organization.pmsy;

import com.everhomes.server.schema.tables.pojos.EhPmsyPayers;
import com.everhomes.util.StringHelper;

public class PmsyPayer extends EhPmsyPayers{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
