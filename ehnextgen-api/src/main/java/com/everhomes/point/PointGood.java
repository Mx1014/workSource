// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointGoods;
import com.everhomes.util.StringHelper;

public class PointGood extends EhPointGoods {
	
	private static final long serialVersionUID = 3575719842295159578L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}