// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointRuleToEventMappings;
import com.everhomes.util.StringHelper;

public class PointRuleToEventMapping extends EhPointRuleToEventMappings {
	
	private static final long serialVersionUID = 3690886973182234560L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}