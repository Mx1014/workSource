package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleCategories;
import com.everhomes.util.StringHelper;

public class OfficeCubicleCategory extends EhOfficeCubicleCategories {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1024014722132844300L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
