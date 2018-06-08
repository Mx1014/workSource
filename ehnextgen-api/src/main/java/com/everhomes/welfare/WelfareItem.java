// @formatter:off
package com.everhomes.welfare;

import com.everhomes.server.schema.tables.pojos.EhWelfareItems;
import com.everhomes.util.StringHelper;

public class WelfareItem extends EhWelfareItems {
	
	private static final long serialVersionUID = -714941335995604130L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}