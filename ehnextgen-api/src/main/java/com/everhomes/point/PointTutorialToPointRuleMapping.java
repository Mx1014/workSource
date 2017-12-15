// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointTutorialToPointRuleMappings;
import com.everhomes.util.StringHelper;

public class PointTutorialToPointRuleMapping extends EhPointTutorialToPointRuleMappings {
	
	private static final long serialVersionUID = 5945933024686471306L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}