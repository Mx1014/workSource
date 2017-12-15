// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointSystems;
import com.everhomes.util.StringHelper;

public class PointSystem extends EhPointSystems {
	
	private static final long serialVersionUID = -7010604801468852284L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}