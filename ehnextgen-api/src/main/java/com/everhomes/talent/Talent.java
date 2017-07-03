// @formatter:off
package com.everhomes.talent;

import com.everhomes.server.schema.tables.pojos.EhTalents;
import com.everhomes.util.StringHelper;

public class Talent extends EhTalents {
	
	private static final long serialVersionUID = -6673080622355930737L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}