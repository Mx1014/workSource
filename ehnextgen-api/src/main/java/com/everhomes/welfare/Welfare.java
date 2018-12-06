// @formatter:off
package com.everhomes.welfare;

import com.everhomes.server.schema.tables.pojos.EhWelfares;
import com.everhomes.util.StringHelper;

public class Welfare extends EhWelfares {
	
	private static final long serialVersionUID = -9137235290705690028L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}