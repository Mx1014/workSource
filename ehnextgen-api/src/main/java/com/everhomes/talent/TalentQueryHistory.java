// @formatter:off
package com.everhomes.talent;

import com.everhomes.server.schema.tables.pojos.EhTalentQueryHistories;
import com.everhomes.util.StringHelper;

public class TalentQueryHistory extends EhTalentQueryHistories {
	
	private static final long serialVersionUID = -6093280037076995190L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}