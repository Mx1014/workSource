// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointCategories;
import com.everhomes.util.StringHelper;

public class PointCategory extends EhPointCategories {
	
	private static final long serialVersionUID = 7753511100883611755L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}