// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointTutorials;
import com.everhomes.util.StringHelper;

public class PointTutorial extends EhPointTutorials {
	
	private static final long serialVersionUID = 5856814184697754373L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}