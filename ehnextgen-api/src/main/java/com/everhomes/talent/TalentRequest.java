// @formatter:off
package com.everhomes.talent;

import com.everhomes.server.schema.tables.pojos.EhTalentRequests;
import com.everhomes.util.StringHelper;

public class TalentRequest extends EhTalentRequests {
	
	private static final long serialVersionUID = 9129267221631613879L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}